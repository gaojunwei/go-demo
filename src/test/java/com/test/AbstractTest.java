package com.test;

import com.gjw.go.GoApp;
import com.gjw.go.test.H2Flusher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author ext.gaojunwei1
 * @date 2023/5/29
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GoApp.class)
@ActiveProfiles({"local", "junit"})
public class AbstractTest {
    @Autowired(required = false)
    protected H2Flusher h2Flusher;

    @BeforeEach
    public void setUp() {
        if (h2Flusher == null) {
            throw new RuntimeException("请使用H2内存数据库作为数据源");
        }
        // 每次执行单元测试之前都要刷新数据库
        h2Flusher.flushDB();
    }
}
