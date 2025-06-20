/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gjw.demo.nacosdiscoveryprovider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.*;

@RestController
public class EchoServiceController {

    @GetMapping("/echo/{message}")
    @SentinelResource(value = "echo", fallback = "echoFallback")
    public String echo(@PathVariable(name = "message") String message) {
        return message + "[ECHO] : Hello from provider";
    }

    // 限流或降级时调用的方法
    public String echoFallback(String message, BlockException ex) {
        System.out.println("系统繁忙，请稍后再试。");
        return "系统繁忙，请稍后再试。";
    }
}
