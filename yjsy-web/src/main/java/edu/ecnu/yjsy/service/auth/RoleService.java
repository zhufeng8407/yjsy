package edu.ecnu.yjsy.service.auth;

import static edu.ecnu.yjsy.constant.QueryParameter.DOMAIN_LEVEL;
import static edu.ecnu.yjsy.constant.QueryParameter.ROLE_ID;
import static edu.ecnu.yjsy.constant.QueryParameter.ROLE_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.DomainLevel;
import edu.ecnu.yjsy.model.auth.Page;
import edu.ecnu.yjsy.model.auth.PageRepository;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;

@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleRepository repo;

    @Autowired
    private AccountPrivilegeRepository accountPrivilegeRepository;

    @Autowired
    private PageRepository pageRepository;

    /**
     * 获取所有的角色列表
     *
     * @return
     */
    public List<Map<String, Object>> getRole() {
        List<Map<String, Object>> res = new ArrayList<>();
        List<Role> roles = repo.findAll();
        roles.forEach(role -> res.add(toMapRepr(role, false)));
        return res;
    }

    /**
     * 当roleId==-1时,创建新的角色,否则,创建一个新角色。
     *
     * @param roleId
     *            角色对应的ID
     * @param roleName
     *            角色名称
     * @param domainLevel
     *            访问域级别
     * @param pageIds
     *            允许访问的页面的ID
     * @return 所有的角色
     */
    public List<Map<String, Object>> save(Long roleId, String roleName,
            String domainLevel, long[] pageIds) {
        Role role = new Role();
        if (roleId != -1) {
            role = repo.findOne(roleId);
        }
        if (role != null) {
            role.setName(roleName);
            role.setDomainLevel(
                    DomainLevel.valueOf(DomainLevel.class, domainLevel));
            Set<Page> pages = new HashSet<>();
            if (pageIds != null) {
                for (long id : pageIds) {
                    Page page = new Page();
                    page.setId(id);
                    pages.add(page);
                }
            }
            role.setPages(pages);
            repo.save(role);
        }

        return getRole();
    }

    /**
     * 删除一个角色
     *
     * @param id
     *            角色的ID
     * @return 角色列表
     */
    public List<Map<String, Object>> delete(long id) {
        // 首先删除所有账号对应该角色的权限,然后才删除角色
        accountPrivilegeRepository.deleteByRoleId(id);
        // FIXME: 还应该删除帐号和角色的关联
        repo.delete(id);
        return getRole();
    }

    /**
     * 查找一个角色,返回的角色信息中包含其能够访问的页面
     *
     * @param id
     *            角色的ID
     * @return
     */
    public Map<String, Object> findOne(long id) {
        return toMapRepr(repo.findOne(id), true);
    }

    /**
     * 将前端所需的<code>Role</code>信息存储到Map中,便于转换成JSON格式
     *
     * @param role
     * @param needGrantedPages
     * @return
     */
    public static Map<String, Object> toMapRepr(Role role,
            boolean needGrantedPages) {
        Map<String, Object> map = new HashMap<>();
        if (role == null) return map;

        map.put(ROLE_ID, role.getId());
        map.put(ROLE_NAME, role.getName());
        map.put(DOMAIN_LEVEL, role.getDomainLevel().toString());
        if (needGrantedPages) {
            List<Long> ids = new ArrayList<>();
            for (Page page : role.getPages()) {
                ids.add(page.getId());
            }
            map.put("ids", ids);
        }
        return map;
    }

    @Transactional(dontRollbackOn = Exception.class)
    @SuppressWarnings("unchecked")
    public Map<String, Object> batchModify(InputStream inputStream) {
        Map<String, Object> res = new HashMap<>();
        Workbook workbook = null;
        try {
            workbook = createWorkbook(inputStream);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        stateStore.setState("progress", 5);
        Sheet sheet = workbook.getSheetAt(0);
        String titles[] = { "角色名", "菜单ID", "菜单名", "菜单备注" };
        if (!checkFormat(sheet.getRow(0), titles)) {
            res.put("msg", "导入数据不符合格式要求！");
            return res;
        }

        Map<String, Object> roleWithPages = new HashMap<>();
        int errorNumber = 0;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);
            if (r == null || r.getCell(0) == null || r.getCell(1) == null) {
                handleErrorData(r, titles, "role-page", "");
                errorNumber++;
                continue;
            }

            Set<Page> pages = null;
            if (roleWithPages.get(r.getCell(0).toString()) == null) {
                Role role = repo.findByName(r.getCell(0).toString());
                if (role == null) {
                    String error = "**" + r.getCell(0).toString() + "**";
                    r.removeCell(r.getCell(0));
                    r.createCell(0).setCellValue(error);
                    handleErrorData(r, titles, "role-page", "");
                    errorNumber++;
                    continue;
                }
                pages = new HashSet<>();
            } else {
                pages = (Set<Page>) roleWithPages.get(r.getCell(0).toString());
            }
            Page p = pageRepository
                    .findOne(Math.round(r.getCell(1).getNumericCellValue()));
            if (p == null) {
                String error = "**" + r.getCell(1).getNumericCellValue() + "**";
                r.removeCell(r.getCell(1));
                r.createCell(1).setCellValue(error);
                handleErrorData(r, titles, "role-page", "");
                errorNumber++;
                continue;
            }
            pages.add(p);
            roleWithPages.put(r.getCell(0).toString(), pages);
            stateStore.setState("progress", i * 90 / sheet.getLastRowNum());
        }

        List<Role> roles = new ArrayList<>();
        for (String rolename : roleWithPages.keySet()) {
            Role role = repo.findByName(rolename);
            role.setPages((Set<Page>) roleWithPages.get(rolename));
            roles.add(role);
        }
        repo.save(roles);

        stateStore.setState("progress", 100);
        res.put("msg", "数据导入成功!");
        res.put("errorNumber", errorNumber);
        res.put("totalNumber", sheet.getLastRowNum());
        res.put("key", "role-page");
        return res;
    }

    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {

    }

}
