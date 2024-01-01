package com.begcode.monolith;

import java.io.File;
import org.springframework.beans.BeansException;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBootUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBootUtil.applicationContext == null) {
            SpringBootUtil.applicationContext = applicationContext;
        }
    }

    public static String getApplicationPath() {
        ApplicationHome home = new ApplicationHome();
        return home.getDir().toString();
    }

    public static String getApplicationPathEndWithSeparator() {
        ApplicationHome home = new ApplicationHome();
        return home.getDir().toString() + File.separator;
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
