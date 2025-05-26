package com.gjw.code.one.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @RequestMapping("/flux")
    public Flux<String> flux() {
        return Flux.just("Hello", "from", "Reactive", "World!")
                .delayElements(Duration.ofSeconds(1)); // 每秒发送一个元素
    }

    interface PrivateView {}

    record UserGreeting(
            String id,
            Long userId,
            String userName,
            @JsonView(PrivateView.class) String userPwd,
            @JsonView(PrivateView.class) String addr
    ) {
    }

    //@JsonView(value = {PublicView.class})
    @RequestMapping("/greet/public")
    public List<UserGreeting> greetOne() {
        return List.of(
                new UserGreeting("1", 100L, "Alice", "alice123", "Wonderland"),
                new UserGreeting("2", 101L, "Bob", "bob456", "Builderland")
        );
    }

    @JsonView(value = {PrivateView.class})
    @RequestMapping("/greet/private")
    public List<UserGreeting> greetTwo() {
        return List.of(
                new UserGreeting("1", 100L, "Alice", "alice123", "Wonderland"),
                new UserGreeting("2", 101L, "Bob", "bob456", "Builderland")
        );
    }
}
