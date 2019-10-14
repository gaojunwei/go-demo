package com.test.mvc;

import com.gjw.common.enums.SystemCodeEnums;
import com.gjw.common.innovation.TApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author gaojunwei
 * @date 2019/10/14 15:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TApplication.class)
public class MvcTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void test001(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test002() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sys/refresh"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(SystemCodeEnums.SUCCESS.getCode()));
    }
}