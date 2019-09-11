package com.test.dto;

import lombok.Data;

/**
 * @author: gaojunwei
 * @Date: 2019/1/22 10:44
 * @Description:
 */
@Data
public class Epltype {
    private String epl_model;
    private String epl_size;
    private String screen_color;
    private String width;
    private String height;
    private String screen_type;

    public Epltype(String epl_model, String epl_size, String screen_color, String width, String height,String screen_type) {
        this.epl_model = epl_model;
        this.epl_size = epl_size;
        this.screen_color = screen_color;
        this.width = width;
        this.height = height;
        this.screen_type = screen_type;
    }
}