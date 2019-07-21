package com.gjw.innovation.common.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: gaojunwei
 * @Date: 2019/6/12 18:02
 * @Description: 校验工具类
 */
public class ValidatorHelper {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    /**
     * 参数校验
     *
     * @param t 需要校验的参数
     * @param groups 校验分组
     */
    public static <T> List<String> validate(T t,Class<?>... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations;
        if(groups == null || groups.length==0)
            constraintViolations = validator.validate(t);
        else
            constraintViolations = validator.validate(t,groups);
        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            messageList.add(constraintViolation.getMessage());
        }
        return messageList;
    }
}
