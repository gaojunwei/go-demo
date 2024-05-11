package com.iot.mqtt.controller;

import com.iot.mqtt.service.DataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataController {

    @Resource
    private DataService dataService;

    @GetMapping("/init")
    public String initData(){
        try {
            dataService.initData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    @GetMapping("/send")
    public String send(){
        try {
            dataService.sendLocation();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    @GetMapping("/back")
    public String returnCard(){
        try {
            dataService.returnCard();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
}
