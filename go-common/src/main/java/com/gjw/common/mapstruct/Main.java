package com.gjw.common.mapstruct;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        mutile();
    }

    public static void mutile() {
        List<StudentPO> studentPOList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            StudentPO studentPO = StudentPO.builder().id(i).name(String.valueOf(i).concat("_name")).age(i).build();
            studentPOList.add(studentPO);
        }
        List<StudentVO> voList = StudentMapper.INSTANCE.poList2VoList(studentPOList);
        System.out.println(voList);
    }

    public static void single() {
        StudentPO studentPO = StudentPO.builder().id(10).name("test").age(24).className("教室名").build();
        StudentVO studentVO = StudentMapper.INSTANCE.po2Vo(studentPO);
        // StudentVO(id=10, studentName=test, studentAge=24, schoolName=null)
        System.out.println(studentVO);
    }
}
