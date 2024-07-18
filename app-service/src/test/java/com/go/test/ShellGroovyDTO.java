package com.go.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lly
 * @Date: 2023/7/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShellGroovyDTO {
    private Integer a;
    private Integer b;
    private Integer num;

}