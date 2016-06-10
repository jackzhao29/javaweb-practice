package com.coldface.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 类SpringUtils.java的实现描述：Spring加载bean工具类
 * 
 * @author coldface
 * @date 2016年5月30日下午5:39:39
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    public void setApplicationContext(ApplicationContext paramCtx) throws BeansException {
        ctx = paramCtx;
    }

    public static Object getBean(String beanName) {
        return ctx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }
}
