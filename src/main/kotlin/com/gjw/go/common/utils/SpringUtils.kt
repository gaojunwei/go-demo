package com.gjw.go.common.utils

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringUtils : BeanFactoryPostProcessor, ApplicationContextAware {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        SpringUtils.beanFactory = beanFactory
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringUtils.applicationContext = applicationContext
    }

    companion object {
        private lateinit var beanFactory: ConfigurableListableBeanFactory
        private lateinit var applicationContext: ApplicationContext

        fun <T> getBean(name: String): T {
            return applicationContext.getBean(name) as T
        }

        fun <T> getBean(requiredType: Class<T>): T? {
            return applicationContext.getBean(requiredType)
        }

        fun <T> getBean(name: String, requiredType: Class<T>): T? {
            return applicationContext.getBean(name, requiredType)
        }

        fun <T> getBeansOfType(requiredType: Class<T>): Map<String, T> {
            return applicationContext.getBeansOfType(requiredType)
        }
    }
}