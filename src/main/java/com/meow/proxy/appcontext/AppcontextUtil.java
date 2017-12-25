package com.meow.proxy.appcontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 系统初始化时需要进行设置全局Spring上下文
 *
 * @author Alex
 *         date:2017/12/22
 *         email:jwnie@foxmail.com
 */
@Component
public class AppcontextUtil implements ApplicationContextAware {
    private final static Logger LOG = LoggerFactory.getLogger(AppcontextUtil.class);
    private static ApplicationContext applicationContext = null;

    /**
     * 根据beanName生成对应的bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static ApplicationContext getContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AppcontextUtil.applicationContext == null) {
            AppcontextUtil.applicationContext = applicationContext;
            LOG.info("Set applicationContext success!");
        }
    }
}
