package com.gjw.go.config

import org.hibernate.validator.HibernateValidator
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory
import javax.validation.Validation
import javax.validation.Validator

@Configuration
class ValidatorConfiguration {
    @Bean
    fun validator(springFactory: AutowireCapableBeanFactory?): Validator? {
        Validation.byProvider(HibernateValidator::class.java)
            .configure()
            // 快速失败
            .failFast(true)
            // 解决 SpringBoot 依赖注入问题
            .constraintValidatorFactory(SpringConstraintValidatorFactory(springFactory!!))
            .buildValidatorFactory().use { factory -> return factory.validator }
    }
}