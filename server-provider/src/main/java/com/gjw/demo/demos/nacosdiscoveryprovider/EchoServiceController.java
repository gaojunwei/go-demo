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
package com.gjw.demo.demos.nacosdiscoveryprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class EchoServiceController {

    @Value("${user.name.xxx}")
    private String userName;

    @GetMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        System.out.println(userName+"有人调用我 echo：" + message);
        return userName+ "[ECHO] : " + message;
    }

    @GetMapping("/exception")
    public String exception() throws InterruptedException {
        System.out.println("有人调用我 exception");
        TimeUnit.SECONDS.sleep(10);
        int a = 1 / 0;
        return "[ECHO] : exception" + a;
    }
}
