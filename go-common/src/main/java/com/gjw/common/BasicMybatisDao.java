package com.gjw.common;

import java.util.List;
import java.util.Map;

/**
 * 基础DAO
 *
 * @param <T>
 * @param <E>
 */
public interface BasicMybatisDao<T, E> {

    String UPDATE_RECORD = "record";
    String UPDATE_PARAMETER = "example";

    /**
     * 插入新的记录
     *
     * @param product 实体信息
     * @return 影响的行数
     */
    int insert(T product);

    /**
     * 插入新的记录
     *
     * @param product 实体信息
     * @return 影响的行数
     */
    int insertSelective(T product);

    /**
     * 更新记录
     *
     * @param product 实体信息
     * @return 影响的行数
     */
    int updateByPrimaryKeySelective(T product);

    /**
     * 更新记录
     *
     * @param id 主键
     * @return 影响的行数
     */
    void updateByPrimaryKey(long id);

    /**
     * 更新记录
     *
     * @param map 实体信息
     * @return 影响的行数
     */
    int updateByExampleSelective(Map<String, Object> map);

    /**
     * 根据条件更新
     *
     * @param example 条件
     * @return 计数
     */
    int updateByExample(E example);

    /**
     * 根据条件计数
     *
     * @param example 条件
     * @return 计数
     */
    long countByExample(E example);

    /**
     * 根据主键查询实体信息
     *
     * @param id id
     * @return 实体信息
     */
    T selectByPrimaryKey(long id);

    /**
     * 根据条件查询实体信息列表
     *
     * @param example 条件
     * @return 实体信息列表
     */
    List<T> selectByExample(E example);

    /**
     * 根据主键删除实体信息
     *
     * @param id id
     */
    void deleteByPrimaryKey(long id);

    /**
     * 根据条件删除实体信息
     * @param example 条件
     * @return 影响的行数
     */
    long deleteByExample(E example);
}
