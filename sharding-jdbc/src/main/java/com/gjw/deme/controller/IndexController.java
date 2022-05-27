package com.gjw.deme.controller;

import com.gjw.deme.mapper.GoodsMapper;
import com.gjw.deme.mapper.domain.Goods;
import com.google.gson.Gson;
import com.gjw.deme.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("db")
@Slf4j
public class IndexController {

    @Resource
    private Gson gson;
    @Resource
    private GoodsMapper goodsMapper;

    @GetMapping("insert")
    public String test001() {
        Goods goods = new Goods();
        goods.setGname("商品名称");
        goods.setGstatus("未发布");
        goods.setUserId(RandomUtils.nextLong(1, 100));
        System.out.println("保存数据=" + gson.toJson(goods));
        int a = goodsMapper.insert(goods);
        return "success" + a;
    }
}
