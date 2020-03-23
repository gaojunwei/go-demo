package com.test;

import com.gjw.common.utils.img.BMPConverter;
import com.gjw.common.utils.img.enums.DeviceTypeEnums;
import lombok.Data;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author gaojunwei
 * @date 2019/9/17 17:35
 */
public class TestCase {
    String imgFilePathStr = "C:\\Users\\gaojunwei\\Pictures\\gx\\timg.jpg";
    @Test
    public void test001() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(imgFilePathStr));
        BufferedImage bufferedImage1 = BMPConverter.floyd(bufferedImage, DeviceTypeEnums.DEVICE_BW);
        ImageIO.write(bufferedImage1, "png", new File("C:\\Users\\gaojunwei\\Pictures\\gx\\hehe2.png"));
    }

    @Test
    public void  test002(){
        List<User> list1 = new ArrayList<>();
        User user1 = new User(1,"gjw");
        User user2 = new User(2,"gjw");

        list1.add(user1);
        list1.add(user2);

        List<User> list2 = new ArrayList<>();

        User user3 = new User(1,"gjw");
        User user4 = new User(2,"gjw");

        list2.add(user3);
        list2.add(user4);

        System.out.println(list1.equals(list2));
        System.out.println(list1==list2);
        System.out.println();
        System.out.println(user1.equals(user3));
        System.out.println(user2.equals(user4));
        System.out.println(user1==user3);
        System.out.println(user2==user4);
    }

    @Data
    class User{
        Integer age;
        String name;

        public User(Integer age,String name){
            this.age = age;
            this.name = name;
        }
    }
}