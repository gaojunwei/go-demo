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

    @GetMapping("three")
    public int three() {
        return indexService.three();
    }

    @GetMapping("four")
    public int four() {
        return indexService.four();
    }

    @GetMapping("cpu")
    public String cpuHigh() {
        indexService.cpuHigh();
        return "cpuHigh";
    }

    @GetMapping("memory")
    public String memoryHigh() {
        indexService.memoryHigh();
        return "memoryHigh";
    }

    @GetMapping("thread")
    public String deadThread() {
        indexService.deadThread();
        return "deadThread";
    }
}
