package com.ecust.test.spel;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试spel表达式
 */
public class SpelTest {

    @Test
    public void test1() {
        parse("1+2", null, false);
        parse("1/2", null, false);
        parse("1%2", null, false);
        parse("1 between {1,2}", null, false);
        parse("3>4", null, false);
        parse("3<4", null, false);
        //三目运算
        parse("4 between {1,2}?true:false", null, false);
        //字符串判空
        Map<String, String> attrMaps = new HashMap<>();
        attrMaps.put("a2", "");
        parse("#a2==''", attrMaps, false);
        attrMaps.put("a2", null);
        parse("#a2!=null", attrMaps, false);
        attrMaps.put("a2", "abc ");
        parse("(#a2).length()", attrMaps, false);
        attrMaps.put("a2", "abc ");
        parse("(#a2).trim()", attrMaps, false);
        attrMaps.put("a2", "abc");
        parse("(#a2).replace('a','c')", attrMaps, false);
        //正则表达式
        parse("'123' matches '\\d{4}'", null, false);

        //使用模板进行拼接
        Map<String, String> attrMap = new HashMap<>();
        attrMap.put("name", "路人甲java");
        attrMap.put("lesson", "spring高手系列!");
        parse("你好:#{#name},我们正在学习:#{#lesson}", attrMap, true);
        //转为集合
        parse("{'a','b'}", null, false);
        //获取集合长度
        parse("({'a','b'}).size()", null, false);
        //转为对象
        parse("{'attr':'b'}", null, false);
        //获取对象属性值
        parse("({'attr':'b'})['attr']", null, false);
    }

    private void parse(String spelStr, Map<String, String> attrMap, boolean isTemp) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression;
        if (isTemp) {
            ParserContext context = new TemplateParserContext("#{", "}");
            expression = parser.parseExpression(spelStr, context);
        } else {
            expression = parser.parseExpression(spelStr);
        }

        EvaluationContext evaluationContext = new StandardEvaluationContext();
        //设置变量
        if (!CollectionUtils.isEmpty(attrMap)) {
            for (Map.Entry<String, String> entry : attrMap.entrySet()) {
                evaluationContext.setVariable(entry.getKey(), entry.getValue());
            }
        }
        System.out.println(String.format("表达式：%s；\n     解析结果：%s", spelStr, expression.getValue(evaluationContext)));
    }
}
