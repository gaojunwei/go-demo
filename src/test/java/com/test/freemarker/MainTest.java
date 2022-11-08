package com.test.freemarker;

import freemarker.template.*;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaojunwei
 * @date 2019/10/22 11:28
 */
public class MainTest {

    @Test
    public void test001() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 异常处理方式配置
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        // 设置模板文件所在的路径。
        configuration.setDirectoryForTemplateLoading(new File("D:\\cd"));
        // 设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("test.ftl");
        // 创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("eplModel", "740_BWR_480_800_1");

        // 创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        Writer out = new FileWriter(new File("D:\\cd\\hello.txt"));
        // 调用模板对象的process方法输出文件。
        template.process(dataModel, out);
        // 关闭流。
        out.close();
    }
}