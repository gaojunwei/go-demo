package com.go.test;


import cn.hutool.extra.spring.SpringUtil;
import com.go.groovy.groovy.service.GroovyInvokeJavaService;
import groovy.lang.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lly
 * @Date: 2023/7/1
 * <p>
 * 下面这段测试类包含两个，一个是有参数的调用，一个是无参数的调用
 * // 创建GroovyShell实例
 * // 创建Binding对象，用于传递参数和接收结果
 * // 设置参数
 * // 执行Groovy脚本
 * // 获取结果
 */
public class MainTest {

    String groovyStr = "package script\n" +
            "\n" +
            "import com.go.test.ShellGroovyDTO\n" +
            "\n" +
            "/**\n" +
            " * @Author: lly\n" +
            " * @Date: 2023/7/1\n" +
            " */\n" +
            "def cal(int a, int b) {\n" +
            "    ShellGroovyDTO dto = new ShellGroovyDTO()\n" +
            "    dto.setA(a)\n" +
            "    dto.setB(b)\n" +
            "    if (b > 0) {\n" +
            "        dto.setNum(a + b)\n" +
            "    } else {\n" +
            "        dto.setNum(a)\n" +
            "    }\n" +
            "    return dto\n" +
            "};\n" +
            "\n" +
            "cal(a, b)";
    String groovyStr2 = "package script\n" +
            "\n" +
            "import com.go.test.ShellGroovyDTO\n" +
            "\n" +
            "/**\n" +
            " * @Author: lly\n" +
            " * @Date: 2023/7/1\n" +
            " */\n" +
            "def cal(int a, int b) {\n" +
            "    ShellGroovyDTO dto = new ShellGroovyDTO()\n" +
            "    dto.setA(a)\n" +
            "    dto.setB(b)\n" +
            "    if (b > 0) {\n" +
            "        dto.setNum(a - b)\n" +
            "    } else {\n" +
            "        dto.setNum(a)\n" +
            "    }\n" +
            "    return dto\n" +
            "};\n" +
            "\n" +
            "cal(a, b)";

    /**
     * GroovyShell 无参数 demo
     **/
    public static void main(String[] args) {
        //test1();
        test2();
        //test3();
    }

    @Test
    public void test4() throws InstantiationException, IllegalAccessException, IOException, InterruptedException {



        while (true){
            GroovyClassLoader loader = new GroovyClassLoader();
            Class<GroovyObject> groovyClass = loader.parseClass(groovyStr,"sss");
            GroovyObject groovyObject = groovyClass.newInstance();
            Object s = groovyObject.invokeMethod(null, new Object[]{6, 6});
            System.out.println(s);
            TimeUnit.MILLISECONDS.sleep(10);

        }

/*
        Class<GroovyObject> groovyClass2 = loader.parseClass(groovyStr2,"sss");

        GroovyObject groovyObject = groovyClass.newInstance();
        loader.clearCache();


        Object s = groovyObject.invokeMethod("cal", new Object[]{6, 6});
        System.out.println(s);


        GroovyObject groovyObject2 = groovyClass2.newInstance();


        Object s2 = groovyObject2.invokeMethod("cal", new Object[]{6, 6});
        System.out.println(s2);


        System.out.println("------");
        System.out.println( groovyClass);
        System.out.println( groovyClass2);
        System.out.println( groovyClass == groovyClass2);
        System.out.println("------");
        System.out.println( groovyObject);
        System.out.println( groovyObject2);
        System.out.println("******");*/
    }


    public static void test1() {
        String groovyStr = "package script\n" +
                "\n" +
                "import com.go.test.ShellGroovyDTO\n" +
                "\n" +
                "/**\n" +
                " * @Author: lly\n" +
                " * @Date: 2023/7/1\n" +
                " */\n" +
                "\n" +
                "def helloWord() {\n" +
                "    return \"hello groovy\"\n" +
                "}\n" +
                "\n" +
                "helloWord()\n" +
                "\n" +
                "def cal(int a, int b) {\n" +
                "    ShellGroovyDTO dto = new ShellGroovyDTO()\n" +
                "    dto.setA(a)\n" +
                "    dto.setB(b)\n" +
                "    if (b > 0) {\n" +
                "        dto.setNum(a + b)\n" +
                "    } else {\n" +
                "        dto.setNum(a)\n" +
                "    }\n" +
                "    return dto\n" +
                "};\n" +
                "\n" +
                "cal(a , b)";

        // 创建GroovyShell实例
        GroovyShell shell = new GroovyShell();
        Script script = shell.parse(groovyStr);
        Object helloWord = script.invokeMethod("helloWord", null);
        System.out.println(helloWord);

    }

    /** GroovyShell 有参数 demo **/
    public static void test2() {

        String groovyStr = "package script\n" +
                "\n" +
                "import com.go.test.ShellGroovyDTO\n" +
                "\n" +
                "/**\n" +
                " * @Author: lly\n" +
                " * @Date: 2023/7/1\n" +
                " */\n" +
                "def cal(int a, int b) {\n" +
                "    ShellGroovyDTO dto = new ShellGroovyDTO()\n" +
                "    dto.setA(a)\n" +
                "    dto.setB(b)\n" +
                "    if (b > 0) {\n" +
                "        dto.setNum(a + b)\n" +
                "    } else {\n" +
                "        dto.setNum(a)\n" +
                "    }\n" +
                "    return dto\n" +
                "};\n" +
                "\n" +
                "cal(a, b)";

        GroovyClassLoader classLoader = new GroovyClassLoader();
        // 创建GroovyShell实例
        GroovyShell shell = new GroovyShell();

        Script script = shell.parse(groovyStr);



        // 执行Groovy脚本
        Object result = script.invokeMethod("cal",new Object[]{1,2});

        // 获取结果
        ShellGroovyDTO dto = (ShellGroovyDTO) result;
        System.out.println(dto);
    }

    public static void test3() {

        ThreadLocal<String> local = new ThreadLocal<>();
        local.set("123");


        String groovyStr = "package script\n" +
                "\n" +
                "import cn.hutool.extra.spring.SpringUtil\n" +
                "import com.go.groovy.groovy.service.GroovyInvokeJavaService\n" +
                "def cal() {\n" +
                //" def kk = 1/0;"+
                "    GroovyInvokeJavaService groovyInvokeJavaService = SpringUtil.getBean(\"groovyInvokeJavaService\")\n" +
                "    return  groovyInvokeJavaService.groovyInvokeJava();\n" +
                "};\n" +
                "cal()";


        // 创建GroovyShell实例
        GroovyShell shell1 = new GroovyShell();
        Script script =shell1.parse(groovyStr);
        Object result = script.run();


        // 获取结果

        System.out.println(result);
    }
}

