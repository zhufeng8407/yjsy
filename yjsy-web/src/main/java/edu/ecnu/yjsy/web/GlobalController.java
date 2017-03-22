package edu.ecnu.yjsy.web;

import static edu.ecnu.yjsy.constant.Constant.API;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.service.util.GlobalService;

@RestController
public class GlobalController {

    private static final String API_PROGRESS = API + "/progress";

    private static final String API_EXPORT_ERROR = API + "/export-error";

    private static final String API_CLEAR_ERROR = API + "/clear-error";

    @Autowired
    private GlobalService service;

    @RequestMapping(value = API_PROGRESS, method = GET)
    @ResponseBody
    public int getProgress(HttpServletRequest request) {
        int progressValue = 0;
        if (request.getSession().getAttribute("progress") != null)
            progressValue = (int) request.getSession().getAttribute("progress");
        return progressValue;
    }

    @RequestMapping(value = API_EXPORT_ERROR, method = GET)
    @ResponseBody
    public byte[] exportError(@RequestParam String tag,
            HttpServletRequest request) {
        try {
            return service.exportError(tag);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = API_CLEAR_ERROR, method = GET)
    @ResponseBody
    public void clearError(@RequestParam String tag,
            HttpServletRequest request) {
        try {
            request.getSession().removeAttribute(tag);
            request.getSession().setAttribute("progress", 0);
        } catch (Exception e) {
            // 用来捕捉Session中没有队友tag的情况
        }
    }

}
