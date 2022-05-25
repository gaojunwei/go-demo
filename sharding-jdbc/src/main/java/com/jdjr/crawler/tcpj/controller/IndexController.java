package com.jdjr.crawler.tcpj.controller;

import com.google.gson.Gson;
import com.jdjr.crawler.tcpj.mapper.GoodsMapper;
import com.jdjr.crawler.tcpj.mapper.domain.Goods;
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
        goods.setUserId(1L);
        int a = goodsMapper.insert(goods);
        System.out.println("保存结果="+a+"  "+gson.toJson(goods));
        return "success";
    }
}
