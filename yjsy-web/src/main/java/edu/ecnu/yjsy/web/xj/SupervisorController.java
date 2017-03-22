package edu.ecnu.yjsy.web.xj;

import static edu.ecnu.yjsy.constant.Constant.API_XJ;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.POST_IS_GRANTED_BY_ROLES;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.service.xj.SupervisorService;

@RestController
public class SupervisorController {

    private static final String API_DS_PREFIX = API_XJ + "/dsImport";

    private static final String API_DS_UPLOAD = API_DS_PREFIX + "/upload";

    private static final String API_DS_ERROR = API_DS_PREFIX + "/error";

    private static final Logger LOG = LoggerFactory
            .getLogger(SupervisorController.class);

    @Autowired
    private SupervisorService supervisorService;

    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_DS_UPLOAD + "\")")
    @RequestMapping(value = API_DS_UPLOAD, method = POST)
    @ResponseBody
    public Map<String, Object> upload(MultipartHttpServletRequest req) {

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            InputStream data = req.getFile("file").getInputStream();
            result = supervisorService.upload(data, req.getParameter("type"), API_DS_UPLOAD, "POST");
        } catch (Exception e) {
            LOG.error("{}", e);
            result.put("state", 0);
            return result;
        }
        return result;
    }

    @RequestMapping(value = API_DS_ERROR, method = GET)
    @ResponseBody
    public byte[] exportError(@RequestParam String tag,
                              HttpServletRequest request) throws Exception {
        return supervisorService.exportError(tag);
    }

}
