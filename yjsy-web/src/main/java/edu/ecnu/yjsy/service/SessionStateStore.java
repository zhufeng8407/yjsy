package edu.ecnu.yjsy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 用于存储每个session相关的数据
 *
 * @author xiafan
 */
@Service
public class SessionStateStore {

    private static final Logger LOG = LoggerFactory
            .getLogger(SessionStateStore.class);

    private void setStateBySession(String key, Object value) {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        if (attrs != null) {
            HttpSession session = attrs.getRequest().getSession();
            session.setAttribute(key, value);
        } else {
            LOG.error("session doesn't exists");
        }
    }

    public void setState(String key, Object value) {
        setStateBySession(key, value);
    }

    public Object getState(String key) {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        if (attrs == null) {
            LOG.error("session doesn't exists");
            return null;
        } else {
            HttpSession session = attrs.getRequest().getSession();
            return session.getAttribute(key);
        }
    }

    public void removeState(String key) {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
        if (attrs == null) {
            LOG.error("session doesn't exists");
        } else {
            attrs.getRequest().getSession().removeAttribute(key);
        }
    }

}
