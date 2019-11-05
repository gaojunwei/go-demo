package com.test;

import com.gjw.common.utils.img.BMPConverter;
import com.gjw.common.utils.img.enums.DeviceTypeEnums;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
}