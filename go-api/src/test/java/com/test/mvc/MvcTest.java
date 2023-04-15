package com.test.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.innovation.TApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaojunwei
 * @date 2019/10/14 15:02
 */


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = TApplication.class)
@AutoConfigureMockMvc
@Transactional
@Rollback
@Slf4j
public class MvcTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void test001() {
        log.info("启动初始化");
    }

    @Test
    @DisplayName("Sys Get方式请求")
    public void sysGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys/refresh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SystemCodeEnums.SUCCESS.getCode()));
    }
    @Test
    @DisplayName("Sys Post方式请求")
    public void sysPost() throws Exception {
        Map<String,String> param = new HashMap<>();
        param.put("A","jack");
        mockMvc.perform(MockMvcRequestBuilders.post("/sys/refresh")
                        .content(JSON.toJSONString(param))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SystemCodeEnums.SUCCESS.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Sys sysPostAction方式请求")
    public void sysPostAction() throws Exception {
        Map<String,String> param = new HashMap<>();
        param.put("A","jack");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/sys/refresh")
                .content(JSON.toJSONString(param).getBytes())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("a_key","v1","v2")
                .header("b_key","aaa"));

        resultActions.andReturn().getResponse().setCharacterEncoding("utf-8");
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SystemCodeEnums.SUCCESS.getCode()));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andDo(result -> {
            result.getResponse().setCharacterEncoding("utf-8");

            Assertions.assertEquals(SystemCodeEnums.SUCCESS.getCode(), JSONPath.read(result.getResponse().getContentAsString(),"$.code"));
        }).andDo(MockMvcResultHandlers.print());
    }
}