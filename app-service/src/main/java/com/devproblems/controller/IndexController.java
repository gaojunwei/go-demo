package com.devproblems.controller;

import com.go.starter.service.CoderAcademyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private CoderAcademyService coderAcademyService;


    @GetMapping("one")
    public String one() {
        return "success -> " + coderAcademyService.sayHello();
    }
}
