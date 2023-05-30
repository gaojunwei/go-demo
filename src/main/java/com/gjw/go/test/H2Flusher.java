package com.gjw.go.test;

import com.gjw.go.common.utils.H2Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * H2数据库刷新器
 */
@Slf4j
public class H2Flusher implements InitializingBean {

    /**
     * 数据库脚本文件保存路径
     */
    private final Map<String, String> scriptFilePath;

    private final JdbcTemplate jdbcTemplate;

    public H2Flusher(JdbcTemplate jdbcTemplate, String... scriptFiles) {
        this.jdbcTemplate = jdbcTemplate;
        this.scriptFilePath = new HashMap<>();
        for (String scriptFile : scriptFiles) {
            scriptFilePath.put(scriptFile, H2Utils.getH2Script(scriptFile));
        }
    }

    /**
     * 刷新H2数据库
     */
    public void flushDB() {
        jdbcTemplate.execute("drop all objects;");
        for (String key : scriptFilePath.keySet()) {
            jdbcTemplate.execute("runscript from '" + scriptFilePath.get(key) + "'");
        }
    }

    public void exeSql(String filePath) {
        String h2File = H2Utils.getH2Script(filePath);
        jdbcTemplate.execute("runscript from '" + h2File + "'");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("H2Flusher 初始化完毕");
    }
}

