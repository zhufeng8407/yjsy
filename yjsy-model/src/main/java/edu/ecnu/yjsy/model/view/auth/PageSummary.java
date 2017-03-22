package edu.ecnu.yjsy.model.view.auth;

/**
 * @author xiafan
 */

public interface PageSummary {

    public long getId();

    public long getParentPageID();

    public String getPageName();

    public String getUrl();

    public String getDescription();

    public String getAnnotation();

}
