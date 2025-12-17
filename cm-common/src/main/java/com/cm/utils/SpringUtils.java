package com.cm.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author 31373
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {

    /**
     * spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * 设置spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        SpringUtils.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        SpringUtils.applicationContext = applicationContext;
    }

    /**
     *获取对象
     * @param name bean的名称
     *
     * @return 一个以所给名字注册的bean的实例
     */
    @SuppressWarnings("unchecked") // 禁止显示未选中的投射警告
    public static <T> T getBean(String name) throws BeansException{
        return (T) beanFactory.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     * @param clz bean对象类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> clz) throws BeansException{
        return beanFactory.getBean(clz);
    }
}
