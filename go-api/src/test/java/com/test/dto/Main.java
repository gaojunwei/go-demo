package com.test.dto;

import java.util.Arrays;
import java.util.List;

/**
 * @author: gaojunwei
 * @Date: 2019/1/22 10:44
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        List<Epltype> list = Arrays.asList(
                new Epltype("1050_BW_960_640","10.5","黑白","960","640","1"),
                new Epltype("1050_BWR_960_640","10.5","黑白红","960","640","2"),

                new Epltype("750_BW_640_384","7.5","黑白","640","384","1"),
                new Epltype("750_BWR_640_384","7.5","黑白红","640","384","2"),

                new Epltype("580_BW_648_480","5.8","黑白","648","480","1"),
                new Epltype("580_BWR_648_480","5.8","黑白红","648","480","2"),

                new Epltype("420_BW_400_300","4.2","黑白","400","300","1"),
                new Epltype("420_BWR_400_300","4.2","黑白红","400","300","2"),

                new Epltype("213_BW_250_122","2.13","黑白","250","122","1"),
                new Epltype("213_BWR_250_122","2.13","黑白红","250","122","2"),
                new Epltype("213_BWR_212_104","2.13","黑白红","212","104","2"),

                new Epltype("290_BW_296_128","2.9","黑白","296","128","1"),
                new Epltype("290_BWR_296_128","2.9","黑白红","296","128","2")
                );

        String startStr = "INSERT INTO `epl_type` (`epl_model`,`screen_type`,`manufactor`,`width`,`height`,`created_date`,`modified_date`,`operator`,`operator_id`,`epl_size`," +
                "`type_code`,`screen_color`,`supplier_id`) VALUES (";
        String endStr = ");";

        int type_code = 9;

        for (Epltype epltype : list){

            String result = startStr.concat("'").concat(epltype.getEpl_model()).concat("',")
                    .concat("'").concat(epltype.getScreen_type()).concat("',")
                    .concat("'汉朔',")
                    .concat(epltype.getWidth()).concat(",")
                    .concat(epltype.getHeight()).concat(",")
                    .concat("'2019-01-25 18:14:39',")
                    .concat("'2019-01-25 18:14:39',")
                    .concat("'zhangsan',")
                    .concat("1,")
                    .concat("'").concat(epltype.getEpl_size()).concat("',")
                    .concat("'").concat(Integer.toString(type_code)).concat("',")
                    .concat("'").concat(epltype.getScreen_color()).concat("',")
                    .concat("3")



                    .concat(endStr);
            System.out.println(result);
            type_code++;
        }


    }
}