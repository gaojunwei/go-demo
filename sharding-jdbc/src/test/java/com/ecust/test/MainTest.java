package com.ecust.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.DbProviderApp;
import com.jdjr.crawler.tcpj.mapper.GoodsMapper;
import com.jdjr.crawler.tcpj.mapper.domain.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/8/4 17:20
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbProviderApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTest {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private Gson gson;

    /**
     * 查询数据(
     * in (切分键)：针对所属表进行查询，不会查询所有表；
     * in (切分键) and field = xx：针对所属表进行查询，不会查询所有表；
     *in (切分键) or field = xx：查询所有分表；
     */
    @Test
    public void selectIn() {
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.in("gid",736307568489529345L,736307559421444097L);
        query.or().eq("gname","商品名称ss");
        List<Goods> goods = goodsMapper.selectList(query);
        System.out.println("查询结果："+gson.toJson(goods));
    }
    /**
     * 查询数据(查询所有表)
     */
    @Test
    public void selectCon() {
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.eq("gname","商品名称AAA");
        List<Goods> goods = goodsMapper.selectList(query);
        System.out.println("查询结果："+gson.toJson(goods));
    }
    /**
     * 查询数据(根据分片建查询，能够精确查询所在表)
     */
    @Test
    public void selectById() {
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.eq("gid",1653482771609L);
        Goods goods = goodsMapper.selectOne(query);
        System.out.println("查询结果："+gson.toJson(goods));
    }

    /**
     * 保存数据
     */
    @Test
    public void insert() {
        Goods goods = new Goods();
        goods.setGid(System.currentTimeMillis());
        goods.setGname("菜瓜");
        goods.setGstatus("未发布");
        goods.setUserId(2L);

        System.out.println("before insert : "+gson.toJson(goods));
        int a = goodsMapper.insert(goods);
        System.out.println("after insert a="+a+",="+gson.toJson(goods));
    }
}