package com.test.mockito;

import com.gjw.common.exception.AppException;
import com.gjw.common.result.BasicResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationAfterDelay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.MethodName.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MockitoDemo {

    @Mock
    private Random mockRandom;

    @BeforeAll
    public void testBefore(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("mockito非注解测试")
    @Order(0)
    public void test001(){
        System.out.println("a "+this);
        Random mockRandom = Mockito.mock(Random.class);//mock了一个Random对象，返回类型的默认值
        Assertions.assertEquals(0,mockRandom.nextInt());

        //Mockito.doThrow(new AppException("000","异常了")).when(mockRandom).nextInt();
        Mockito.when(mockRandom.nextInt()).thenThrow(new AppException("000","异常了"));//进行打桩操作，指定调用方法
        try {
            //reset方法可以重置之前自定义的返回值和异常
            Mockito.reset(mockRandom);
            Assertions.assertEquals(0,mockRandom.nextInt());
        }catch (AppException e){
            Assertions.assertTrue(e instanceof AppException);
            Assertions.assertEquals("000:异常了",e.getMessage());
            System.out.println(">>>>>>>>>>>>>>>");
        }

        BasicResult basicResult = Mockito.mock(BasicResult.class);
        Mockito.when(basicResult.getCode()).thenReturn("0");
        Assertions.assertEquals("0",basicResult.getCode());
    }

    @Test
    @DisplayName("mockito注解测试")
    @Order(1)
    public void test002(){
        System.out.println("b "+this);
        Assertions.assertEquals(0,mockRandom.nextInt());

        Mockito.when(mockRandom.nextInt()).thenReturn(1);//进行打桩操作，指定调用方法
        Assertions.assertEquals(1,mockRandom.nextInt());

    }

    @Test
    @DisplayName("mockito注解测试2")
    @Order(2)
    public void test003(){
        System.out.println("c "+this);
        Assertions.assertEquals(1,mockRandom.nextInt());
    }
}
