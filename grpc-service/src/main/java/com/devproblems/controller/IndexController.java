package com.devproblems.controller;

import com.devproblems.controller.dto.AuthorVo;
import com.devproblems.controller.dto.BookVo;
import com.devproblems.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/author")
public class IndexController {
    @Resource
    private IndexService indexService;

    @GetMapping("one")
    public AuthorVo one(@RequestParam(name = "param", required = false) String one) {
        return indexService.one(one);
    }

    @GetMapping("two")
    public BookVo two() {
        return indexService.two();
    }
}
