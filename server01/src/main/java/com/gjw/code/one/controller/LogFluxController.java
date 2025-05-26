package com.gjw.code.one.controller;

import com.gjw.code.one.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/cmd")
public class LogFluxController {
    @Resource
    private LogService logService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> grep(
            @RequestParam String file,
            @RequestParam String keyword) {
        return logService.grepWithLinuxCommand(file, keyword);
    }
}
