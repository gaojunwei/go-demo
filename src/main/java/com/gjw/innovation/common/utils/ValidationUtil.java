package com.gjw.innovation.common.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: gaojunwei
 * @Date: 2019/7/18 14:03
 * @Description:
 */
public class ValidationUtil {
    /**
     * 开启快速结束模式 failFast (true)
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 校验对象
     * @param t bean
     * @return ValidResult
     */
    public static <T> ValidResult validateBean(T t) {
        return validateBean(t,null);
    }
    /**
     * 分组-校验对象
     * @param t bean
     * @param groups 校验组
     * @return ValidResult
     */
    public static <T> ValidResult validateBean(T t,Class<?>...groups) {
        Set<ConstraintViolation<T>> violationSet;
        if(groups == null){
            violationSet = validator.validate(t);
        }else{
            violationSet = validator.validate(t,groups);
        }
        return bindResult(violationSet);
    }

    /**
     * 校验bean的某一个属性
     * @param obj          bean
     * @param propertyName 属性名称
     * @return ValidResult
     */
    public static <T> ValidResult validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> violationSet = validator.validateProperty(obj, propertyName);
        return bindResult(violationSet);
    }

    /**
     * 结果封装
     */
    private static <T> ValidResult bindResult(Set<ConstraintViolation<T>> violationSet){
        ValidResult result = new ValidationUtil().new ValidResult();
        boolean hasError = violationSet != null && violationSet.size() > 0;
        result.setHasErrors(hasError);
        if (hasError) {
            for (ConstraintViolation<T> violation : violationSet) {
                result.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return result;
    }

    /**
     * 校验结果类
     */
    @Getter
    public class ValidResult {
        /**
         * 是否有错误
         */
        @Setter
        private boolean hasErrors;

        /**
         * 错误信息
         */
        private List<ErrorMessage> errors = new ArrayList<>();

        /**
         * 添加错误信息
         */
        void addError(String propertyName, String message) {
            this.errors.add(new ErrorMessage(propertyName, message));
        }
    }

    /**
     * 错误消息封装实体
     */
    @Data
    public class ErrorMessage {
        private String property;
        private String message;

        ErrorMessage(String propertyPath, String message) {
            this.property = propertyPath;
            this.message = message;
        }
    }
}