package com.jdjr.crawler.tcpj.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jdjr.crawler.tcpj.common.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述
 *
 * @author gaojunwei
 * @date 2020/7/7 11:00
 **/
public class JSRunnerFilter {
    private static final Logger logger = LoggerFactory.getLogger(JSRunnerFilter.class);
    /**
     * 匹配EL表达式
     */
    private static final Pattern PATTERN_FIELD = Pattern.compile("\\$\\{.+?}");

    /**
     * 缓存执行脚本
     */
    private static LoadingCache<String, ScriptEngine> engineCache = CacheBuilder.newBuilder()
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ScriptEngine>() {
                @Override
                public ScriptEngine load(String s) throws Exception {
                    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                    engine.eval(s);
                    return engine;
                }
            });

    /**
     * 运行JS脚本
     *
     * @param funName  函数名称
     * @param param    入参（多个参数使用“,”逗号分隔）
     * @param script   JS脚本
     * @param input    in
     * @param sliptStr a
     * @return r
     */
    public static String jsExe(String funName, String param,
                               String sliptStr, String script, Map<String, Object> input) {
        if (StringUtils.isEmpty(funName) || StringUtils.isEmpty(script)) {
            throw new AppException("9", "参数不合法，不能为空");
        }
        String result = "";
        //取出参数
        param = param == null ? "" : param;
        String[] params;
        if (sliptStr == null || sliptStr.trim().equals("")) {
            params = new String[]{param};
        } else {
            params = param.split(sliptStr, -1);
        }
        //解析参数
        for (int i = 0; i < params.length; i++) {
            params[i] = analysis(params[i], input);
        }
        //执行脚本
        try {
            result = callJavaScript(funName, params, script);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new AppException("9", StringUtils.isEmpty(e.getMessage()) ? "JS脚本执行异常" : e.getMessage());
        }
        return result;
    }

    /**
     * 执行脚本
     *
     * @param funName 函数名称
     * @param params  入参参数
     * @param script  脚本
     * @return https://www.cnblogs.com/top8/p/6207945.html
     */
    private static String callJavaScript(String funName, String[] params, String script) throws ScriptException, NoSuchMethodException {
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
//
//        engine.eval(script);
        ScriptEngine engine = getEngineFromCache(script);
        Invocable in = (Invocable) engine;
        Object obj = in.invokeFunction(funName, params);
        if (obj == null) {
            return null;
        }
        if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Long || obj instanceof Boolean) {
            return String.valueOf(obj);
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 解析el表达式
     *
     * @param elStr s
     * @param input s
     * @return r
     */
    private static String analysis(String elStr, Map<String, Object> input) {
        String contentAfter = "";
        if (elStr != null) {
            contentAfter = elStr;
            if (input != null && input.size() > 0) {
                Matcher matchField = PATTERN_FIELD.matcher(contentAfter);
                while (matchField.find()) {
                    String field = matchField.group();
                    String keyword = field.substring(field.indexOf("${") + 2, field.indexOf("}"));
                    String fieldValue = (String) input.get(keyword);
                    if (fieldValue != null) {
                        contentAfter = contentAfter.replace(field, fieldValue);
                    }
                }
            }
        }
        return contentAfter;
    }

    /**
     * 返回ScriptEngine 对象
     *
     * @param script 执行的脚本
     * @return r
     */
    private static ScriptEngine getEngineFromCache(String script) {
        ScriptEngine engine = null;
        try {
            engine = engineCache.get(script);
        } catch (ExecutionException e) {
//            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        return engine;
    }
}
