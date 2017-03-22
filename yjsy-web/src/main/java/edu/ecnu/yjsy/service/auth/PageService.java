package edu.ecnu.yjsy.service.auth;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import edu.ecnu.yjsy.model.auth.Page;
import edu.ecnu.yjsy.model.auth.PageRepository;
import edu.ecnu.yjsy.model.view.auth.PageSummary;
import edu.ecnu.yjsy.security.authentication.GrantedDomainAuthority;

/**
 * 实现菜单管理的服务，主要提供以下功能： 1. 基于用户当前使用的角色，构建他能够访问的访问菜单 2. 返回所有的菜单项，用于管理菜单 3.
 * 提供菜单的搜索服务
 *
 * @author guhang
 * @author xiafan
 * @author sunchen
 */
@Component
public class PageService {

    private static final String PAGE_ID = "id";

    private static final String PAGE_URL = "url";

    private static final String PAGE_NAME = "name";

    private static final String PAGE_ICON = "icon";

    private static final String PARENT_PAGE_ID = "pid";

    private static final String CHILD_PAGES = "subMenu";

    private static final String ANNOTATION = "annotation";

    @Autowired
    private PageRepository pagePrivilegeRepo;

    private Map<String, Set<PageSummary>> index = new HashMap<>();

    private Comparator<Map.Entry<PageSummary, Integer>> pageComparator = new Comparator<Map.Entry<PageSummary, Integer>>() {
        public int compare(Entry<PageSummary, Integer> o1,
                Entry<PageSummary, Integer> o2) {
            if ((o2.getValue() - o1.getValue()) == 0) {
                PageSummary p1 = o1.getKey();
                PageSummary p2 = o2.getKey();
                int i = p1.getPageName().length() - p2.getPageName().length();
                if (i == 0) {
                    Collator instance = Collator.getInstance(Locale.CHINA);
                    return instance.compare(p1.getPageName(), p2.getPageName());
                }
                return i;
            } else {
                return o2.getValue() - o1.getValue();
            }
        }
    };

    /**
     * FIXME:增加定期更新
     * <p>
     * 构建索引
     */
    public void constructIndex() {
        List<PageSummary> pages = pagePrivilegeRepo.queryAll();
        if (pages.isEmpty()) return;
        for (PageSummary page : pages) {
            Set<String> grams = new HashSet<>();
            for (int i = 0; i < page.getPageName().length(); i++) {
                grams.add(page.getPageName().substring(i, i + 1));
            }
            for (int i = 0; i < page.getPageName().length() - 1; i++) {
                grams.add(page.getPageName().substring(i, i + 2));
            }
            for (String gram : grams) {
                index.putIfAbsent(gram, new HashSet<PageSummary>());
                index.get(gram).add(page);
            }
        }
    }

    /**
     * 基于用户当前的活跃角色，构建他能够访问的菜单结构
     *
     * @return 按照保留菜单之间父子关系的菜单项列表
     */
    public List<Map<String, Object>> getMenu() {
        Set<Page> pages = new HashSet<Page>();
        for (GrantedAuthority authority : SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()) {
            if (authority instanceof GrantedDomainAuthority)
                pages.addAll(((GrantedDomainAuthority) authority)
                        .getAccountPrivilege().getRole().getPages());
        }
        if (pages.isEmpty()) return new ArrayList<>();
        return constructMenu(pages);
    }

    /**
     * 返回所有的菜单项
     *
     * @return
     */
    public List<Map<String, Object>> getPages() {
        return constructMenu(new HashSet<Page>());
    }

    /**
     * 基于给定的URL菜单项，获取他们的所有祖先菜单项，从而构建出树形菜单
     *
     * @param pages
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> constructMenu(Set<Page> pages) {
        if (pages == null || pages.isEmpty()) {
            pages = new HashSet<Page>(pagePrivilegeRepo.findAll());
        }

        Map<Long, Map<String, Object>> siteMap = new HashMap<>();
        pages.forEach(page -> siteMap.put(page.getId(), frontEndRep(page)));

        Set<Long> workingPages = new HashSet<Long>(siteMap.keySet());
        Set<Long> pendingPages = new HashSet<Long>();
        Set<Long> rootPages = new HashSet<Long>();
        while (!workingPages.isEmpty()) {
            Iterator<Long> iterator = workingPages.iterator();
            while (iterator.hasNext()) {
                Long curPageID = iterator.next();
                Map<String, Object> curPage = siteMap.get(curPageID);
                Long parentId = (Long) curPage.get(PARENT_PAGE_ID);

                if (parentId != -1) {
                    if (siteMap.get(parentId) == null) {
                        Page pPage = pagePrivilegeRepo.findOne(parentId);
                        siteMap.put(pPage.getId(), frontEndRep(pPage));
                        if (!workingPages.contains(parentId))
                            pendingPages.add(parentId);
                    }

                    ((List<Map<String, Object>>) siteMap.get(parentId)
                            .get(CHILD_PAGES)).add(curPage);
                } else {
                    rootPages.add(curPageID);
                }
            }
            workingPages = pendingPages;
            pendingPages = new HashSet<>();
        }

        List<Map<String, Object>> menu = new ArrayList<Map<String, Object>>();
        rootPages.forEach(id -> {
            setupDefaultURLofInternPage(siteMap.get(id));
            menu.add(siteMap.get(id));
        });

        return menu;
    }

    /**
     * 设置内部页面的URL为其第一个子页面的URL
     *
     * @param page
     */
    @SuppressWarnings("unchecked")
    private void setupDefaultURLofInternPage(Map<String, Object> page) {
        if (page.containsKey(CHILD_PAGES)) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) page
                    .get(CHILD_PAGES);
            if (children.size() > 0) {
                page.put(PAGE_URL, children.get(0).get(PAGE_URL));
                children.forEach(child -> setupDefaultURLofInternPage(child));
            }
        }
    }

    /**
     * 获取<code>Page</code>的前端表示
     *
     * @param page
     * @return
     */
    private Map<String, Object> frontEndRep(Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PAGE_NAME, page.getPageName());
        map.put(PAGE_ID, page.getId());
        map.put(PAGE_ICON, page.getIconClass());
        map.put(PAGE_URL, page.getUrl());
        map.put(PARENT_PAGE_ID, page.getParentPageID());
        map.put(CHILD_PAGES, new ArrayList<Map<String, Object>>());
        map.put(ANNOTATION, page.getAnnotation());
        return map;
    }

    public List<Map<String, Object>> getPage(String keywords) {
        List<Map<String, Object>> rtn = new ArrayList<>();
        Set<Page> names = new HashSet<>();
        for (GrantedAuthority authority : SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()) {
            for (Page page : ((GrantedDomainAuthority) authority)
                    .getAccountPrivilege().getRole().getPages())
                names.add(page);
        }

        List<PageSummary> pages = new ArrayList<>();
        Map<PageSummary, Integer> counter = new HashMap<>();
        if (keywords.length() <= 2) {
            if (index.containsKey(keywords)) {
                pages.addAll(index.get(keywords));
            }
            for (PageSummary page : pages) {
                counter.put(page, 1);
            }
        } else {
            for (int i = 0; i < keywords.length() - 1; i++) {
                String twoGram = keywords.substring(i, i + 2);
                if (index.containsKey(twoGram)) {
                    Set<PageSummary> candidatePages = index.get(twoGram);
                    for (PageSummary page : candidatePages) {
                        if (!counter.containsKey(page))
                            counter.put(page, 1);
                        else
                            counter.put(page, counter.get(page) + 1);
                    }
                }
            }

            List<Map.Entry<PageSummary, Integer>> list = new ArrayList<Map.Entry<PageSummary, Integer>>(
                    counter.entrySet());
            Collections.sort(list, pageComparator);
            for (Map.Entry<PageSummary, Integer> mapping : list) {
                pages.add(mapping.getKey());
            }
        }

        for (PageSummary page1 : pages) {
            Page page = new Page();
            page.setPageName(page1.getPageName());
            page.setUrl(page1.getUrl());
            page.setId(page1.getId());
            page.setParentPageID(page1.getParentPageID());
            if (names.contains(page)) {
                rtn.add(frontEndRep(getFolder(page)));
            }
        }

        return rtn;
    }

    private Page getFolder(Page page) {
        Page parentPage = pagePrivilegeRepo.findOneById(page.getParentPageID());
        while (parentPage != null) {
            page.setPageName(
                    parentPage.getPageName() + "->" + page.getPageName());
            parentPage = pagePrivilegeRepo
                    .findOneById(parentPage.getParentPageID());
        }
        return page;
    }

}
