package com.devproblems.cofig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class SysConfig {

    @Value("${gjw.test}")
    private String configVal;

    private Integer limit = 10;
}
