package com.jdjr.crawler.tcpj.hooks.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

public class MyProcessor implements BeanFactoryPostProcessor, ImportBeanDefinitionRegistrar {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
