package com.ahucoding.rocket.callmcpserver.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jianzhang
 * 2025/03/18/下午8:00
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String chat(Model model) {
        return "index";
    }
}
