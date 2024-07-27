package com.fenseaizi.wmsdemo.common;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class GetPath {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取项目路径
     *
     * @return 项目路径
     * @throws IOException 当获取路径时发生IO异常
     */
    public String getPath() throws IOException {
        ResourceLoader resourceLoader = applicationContext;
        Resource resource = resourceLoader.getResource("classpath:");
        String path =
                resource.getFile().getParentFile().getParentFile().getParentFile().getAbsolutePath();
        return path;
    }

    /**
     * 获取项目中静态资源的绝对路径
     *
     * @return 静态资源绝对路径
     * @throws IOException 当获取路径时发生IO异常
     */
    public String getProjectAbsolutePath() throws IOException {
        return getPath() + "\\wmsDemo\\src\\main\\resources\\static";
    }
}