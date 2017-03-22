package edu.ecnu.yjsy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.ecnu.yjsy.service.auth.PageService;
import edu.ecnu.yjsy.util.AuthUtil;
import edu.ecnu.yjsy.util.DataUtil;
import edu.ecnu.yjsy.util.MetadataUtil;

/**
 * 用于加载系统所需要的数据。
 * 
 * @author xulinhao
 * @author xiafan
 */

@Component
public class ApplicationInitializer {

    private static final Logger LOG = LoggerFactory
            .getLogger(ApplicationInitializer.class);

    // XXX
    // 更改 /src/main/resources/application.properties 文件中的键值
    // 用于测试数据的加载
    @Value("${ecnu.yjsy.debug}")
    boolean DEBUG;

    @Autowired
    private MetadataUtil metadataUtil;

    @Autowired
    private DataUtil dataUtil;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private PageService pageService;

    @Autowired
    public void run() {
        if (DEBUG) {
            LOG.info(">>>开始加载元数据");
            metadataUtil.load();

            LOG.info(">>>开始加载学籍数据");
            dataUtil.load();

            LOG.info(">>>创建超级管理员账户");
            authUtil.createAdmin();
        }

        // 用于建立菜单的快捷搜索
        pageService.constructIndex();
    }

}
