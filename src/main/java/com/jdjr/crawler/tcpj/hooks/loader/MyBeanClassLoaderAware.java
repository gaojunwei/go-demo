package com.jdjr.crawler.tcpj.hooks.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyBeanClassLoaderAware implements ResourceLoaderAware, EnvironmentAware, InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, ApplicationContextAware {

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        logger.info("{}", classLoader);
    }

    /**
     * InitializingBean
     * afterPropertiesSet方法里面可以添加自定义的初始化方法或者做一些资源初始化操作。当BeanFactory设置完所有的Bean属性之后才会调用#afterPropertiesSet方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("初始化资源");
    }

    /**
     * DisposableBean
     * destroy可以添加自定义的一些销毁方法或者资源释放操作
     */
    @Override
    public void destroy() throws Exception {
        logger.info("释放资源");
    }

    /**
     * BeanNameAware
     * 获取bean在容器中的名字
     */
    @Override
    public void setBeanName(String name) {
        logger.info("setBeanName name:{}", name);
    }

    private BeanFactory beanFactory;

    /**
     * BeanFactoryAware
     * 要直接在自己的代码中读取spring的bean,我们除了根据常用的set外,也可以通过spring的BeanFactoryAware接口实现,只要实现setBeanFactory方法就可以(提供动态的去获取对象的能力);
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        logger.info("setBeanFactory beanFactory:{}", beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("setApplicationContext applicationContext:{}", applicationContext);
    }

    /**
     * EnvironmentAware
     * 读取或者修改Environment的变量
     */
    @Override
    public void setEnvironment(Environment environment) {
        logger.info("setEnvironment");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        logger.info("setResourceLoader");
    }
}
