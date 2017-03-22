package edu.ecnu.yjsy.security.authorization;

import edu.ecnu.yjsy.model.auth.RestAPIRepository;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.StudentRepository;

import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.ecnu.yjsy.security.util.QueryParameter.CONDITION;
import static edu.ecnu.yjsy.security.util.QueryParameter.STUDENT_NO;

/**
 * 当前实现类能够基于访问URL，结合从数据库中加载的访问当前URL需要的权限,判断当前用户是否有 权限方法当前API接口.
 * 该方法在服务器启动时,调用<code>register</code>函数,通过传入的
 * <code>RequestMappingHandlerMapping</code>,获得所有的endpoints(即api),基于这些
 * endpoints,采用FastRoute<url>http://nikic.github.io/2014/02/18/Fast-request-routing-using-regular-expressions.html</url>
 * 的原理,构建正则表达式,实现从url到相应controller定义的pattern的快速映射
 *
 * @author xiafan
 */
@Component
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DomainAccessDecisionManager implements AccessDecisionManager {
    private static final String PERMIT_ALL = "permitAll";

    private static final String AUTHENTICATED = "authenticated";

    private static final String AUTHORIZED = "authorized";

    private final static Logger LOG = Logger
            .getLogger(DomainAccessDecisionManager.class);

    // 用于抽取路径pattern中定义的变量,例如/xj/students/{sno}中的sno
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern
            .compile("\\{[^/]+?\\}");

    private static final String PATH_VARIABLE_REPLACE = "([^/]+)";

    @Autowired
    private RestAPIRepository restAPIRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StaffRepository staffRepository;

    // 基于正则表达的路由方法,通过这则表达式判断哪个路由被命中,然后通过对应方法中对应的
    // index获取对应的<code>RequestPatternInfo</code>
    private Map<RequestMethod, Map<Integer, RequestPatternInfo>> regexRoutes = new HashMap<>();

    // 直接实现URL到相应pattern的映射方式
    private Map<RequestMethod, Map<String, RequestPatternInfo>> staticRoutes = new HashMap<>();

    // 不同方法对应的路由正则表达式
    private Map<RequestMethod, Pattern> regexRoutePatterns = new HashMap<>();

    private boolean decideFreflight(HttpServletRequest request, Authentication authentication,
                                    Collection<ConfigAttribute> configAttributes) {
        String httpMethod = request.getMethod().toUpperCase();
        // 直接pass OPTIONS请求,这是类似于preflight的跨域预请求,不能基于权限拒绝前端请求
        // 否则前端无法捕捉到access denied的异常,也就无法区分后端
        if (httpMethod.equals("OPTIONS")) return true;

        // 这些属性在 WebSecurityConfig中使用方法配置，例如permitAll(), authenticated()
        for (ConfigAttribute attr : configAttributes) {
            // permitAll就直接通过授权
            if (attr.toString().equals("permitAll")
                    || attr.toString().equals("anonymous")) {
                return true;
            }
        }

        // 如果未授权就直接拒绝方法,之所以这样参照链接
        // http://stackoverflow.com/questions/26101738/why-is-the-anonymoususer-authenticated-in-spring-security
        if (authentication instanceof AnonymousAuthenticationToken
                || !authentication
                .isAuthenticated()) {
            throw new AccessDeniedException(
                    "对不起，您尚未登录！");
        }
        return false;
    }

    /**
     * @param authentication   当前用户的授权信息
     * @param object           本类对应的为<code>FilterInvocation</code>对象,获取回去到请求信息
     * @param configAttributes 也就是<code>SecurityConfig</code>中permitAll(), access()等函数配置的属性
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        final HttpServletRequest request = ((FilterInvocation) object)
                .getRequest();

        if (decideFreflight(request, authentication, configAttributes))
            return;

        DomainSecurityExpressionRoot root = new DomainSecurityExpressionRoot(
                authentication, restAPIRepo, studentRepo, staffRepository);

        String httpMethod = request.getMethod().toUpperCase();
        String apiUrl = request.getServletPath();
        String errorMsg = String.format(
                "无法访问服务 %s, 使用的方法是 %s", httpMethod, apiUrl);
        RequestInfo patternInfo = getRequestPatternInfo(request);
        if (patternInfo == null) {
            throw new AccessDeniedException(
                    "服务地址不存在！");
        }

        try {
            String sno = request.getParameter(STUDENT_NO);
            if (sno == null) {
                sno = patternInfo.getVariable(STUDENT_NO);
            }
            if (sno != null) {
                if (!root.hasAccessToStudent(httpMethod,
                        patternInfo.patternInfo.pathPattern,
                        sno)) {
                    throw new AccessDeniedException(errorMsg);
                }
            } else if (request.getParameter(CONDITION) != null) {
                if (!root.hasDomainsPrivilegeByCondition(httpMethod,
                        patternInfo.patternInfo.pathPattern,
                        request.getParameter(
                                CONDITION))) {
                    throw new AccessDeniedException(
                            errorMsg);
                }
            } else {
                // 默认采用基于角色的过滤方法
                if (!root.isGrantedByRoles(httpMethod,
                        patternInfo.patternInfo.pathPattern)) {
                    throw new AccessDeniedException(
                            errorMsg);
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            throw new AccessDeniedException(ex.getMessage());
        }
    }

    /**
     * 用于表达请求路径的模式,请求方法以及请求路径中包含的变量名
     */
    private static class RequestPatternInfo {
        final String pathPattern;
        final List<String> variables;

        RequestPatternInfo(String pathPattern, List<String> variables) {
            this.pathPattern = pathPattern;
            this.variables = variables;
        }
    }

    /**
     * 用于表示具体的一次请求对应的<code>RequestPatternInfo</code>,已经路径中的变量值
     */
    private static class RequestInfo {
        RequestPatternInfo patternInfo;
        List<String> variableValue;

        RequestInfo(RequestPatternInfo patternInfo,
                    List<String> variableValue) {
            this.patternInfo = patternInfo;
            this.variableValue = variableValue;
        }

        String getVariable(String variable) {
            if (variableValue.size() == 0) return null;
            for (int i = 0; i < patternInfo.variables.size(); i++) {
                if (patternInfo.variables.get(i)
                        .equals(variable)) {
                    return variableValue.get(i);
                }
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private RequestInfo getRequestPatternInfo(HttpServletRequest request) {
        RequestPatternInfo mapping;
        RequestInfo ret = null;

        String lookupPath = request.getServletPath();
        RequestMethod requestMethod = RequestMethod
                .valueOf(request.getMethod());

        // static routes
        Map<String, RequestPatternInfo> routeMap = staticRoutes
                .get(requestMethod);
        if (routeMap != null
                && routeMap.get(lookupPath) != null) {
            return new RequestInfo(
                    routeMap.get(lookupPath), ListUtils.EMPTY_LIST);
        }

        // 如果静态路由找不到,则需要使用更加耗时的正则路由
        Matcher matcher = regexRoutePatterns.get(requestMethod)
                .matcher(lookupPath);

        if (matcher.matches()) {
            int i;
            for (i = 1; matcher.group(i) == null; i++) ;
            mapping = regexRoutes.get(requestMethod).get(i);

            String uriVariable;
            List<String> variables = new ArrayList<>();
            while (++i <= matcher.groupCount()
                    && (uriVariable = matcher.group(i)) != null) {
                variables.add(uriVariable);
            }
            ret = new RequestInfo(mapping, variables);
        }

        return ret;
    }

    @Autowired
    private void register(
            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        Map<RequestMethod, Integer> indexes = new HashMap<>();
        Map<RequestMethod, StringBuilder> patternBuilders = new HashMap<>();
        for (RequestMappingInfo mapping : requestMappingHandlerMapping
                .getHandlerMethods().keySet()) {
            // http method -> look pathPattern -> mapping
            Set<RequestMethod> methods = mapping.getMethodsCondition()
                    .getMethods();
            if (methods.isEmpty()) {
                methods = new HashSet<>(Arrays.asList(RequestMethod.values()));
            }
            for (RequestMethod requestMethod : methods) {
                for (String path : mapping.getPatternsCondition()
                        .getPatterns()) {
                    Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);

                    // 首先找出path中所有的变量
                    boolean find = false;
                    List<String> uriVariableNames = new ArrayList<>();
                    while (matcher.find()) {
                        if (!find) {
                            find = true;
                        }
                        String group = matcher.group(0);
                        uriVariableNames
                                .add(group.substring(1, group.length() - 1));
                    }

                    /**
                     * 如果找到的话,就放到基于正则表达式的路由中去,否则,直接放入静态路由。
                     * 静态路由由于没有变量在路径中,可以直接通过路径进行匹配
                     */
                    if (find) {
                        if (regexRoutes.get(requestMethod) == null) {
                            regexRoutes.put(requestMethod,
                                    new HashMap<Integer, RequestPatternInfo>());
                            patternBuilders.put(requestMethod,
                                    new StringBuilder("^"));
                            indexes.put(requestMethod, 1);
                        }
                        int i = indexes.get(requestMethod);
                        regexRoutes.get(requestMethod).put(i,
                                new RequestPatternInfo(path, uriVariableNames));
                        indexes.put(requestMethod,
                                i + uriVariableNames.size() + 1);
                        patternBuilders.get(requestMethod).append("(")
                                .append(matcher
                                        .replaceAll(PATH_VARIABLE_REPLACE))
                                .append(")|");
                    } else {
                        staticRoutes.putIfAbsent(requestMethod,
                                new HashMap<String, RequestPatternInfo>());
                        staticRoutes.get(requestMethod).put(path,
                                new RequestPatternInfo(path,
                                        new ArrayList<String>()));
                    }
                }
            }
        }
        for (Map.Entry<RequestMethod, StringBuilder> entry : patternBuilders
                .entrySet()) {
            RequestMethod method = entry.getKey();
            StringBuilder patternBuilder = entry.getValue();
            if (patternBuilder.length() > 1) {
                patternBuilder.setCharAt(patternBuilder.length() - 1, '$');
            }
            regexRoutePatterns.put(method,
                    Pattern.compile(patternBuilder.toString()));
        }

        LOG.info("load url pattern information completes");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        if (attribute.toString().equals(PERMIT_ALL)
                || attribute.toString().equals(AUTHENTICATED)
                || attribute.toString().equals(AUTHORIZED))
            return true;
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        LOG.debug(clazz.toString());
        return true;
    }

}