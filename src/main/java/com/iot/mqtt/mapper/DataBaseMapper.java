package com.iot.mqtt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DataBaseMapper {
    List<Map<String, Object>> list(@Param("sqlStr") String sqlStr);

    int add(@Param("code_sn") String code_sn,@Param("card_number") Long card_number);

    int exist(@Param("code_sn") String code_sn,@Param("card_number") Long card_number);
}
