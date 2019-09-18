package com.gjw.common.utils.img;

import com.gjw.common.utils.img.enums.DeviceTypeEnums;

import java.awt.image.BufferedImage;

/**
 * 使用Floyd-Steinberg抖动算法处理设备显示的图片
 * 参考网址:https://my.oschina.net/u/4042451/blog/3058233
 * @author gaojunwei
 * @date 2019/9/17 13:17
 */
public class BMPConverter {

    /**
     * 图片抖动算法使用
     * @param sourceImg
     * @param deviceTypeEnums
     * @return
     */
    public static BufferedImage floyd(BufferedImage sourceImg,DeviceTypeEnums deviceTypeEnums){
        /***/
        int width = sourceImg.getWidth();//图片宽度
        int height = sourceImg.getHeight();//图片高度
        RGBTriple[][] rgbTriples = new RGBTriple[width][height];

        int rgbR;
        int rgbG;
        int rgbB;

        int minx = sourceImg.getMinX();
        int miny = sourceImg.getMinY();

        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = sourceImg.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                rgbR = (pixel & 0xff0000) >> 16;
                rgbG = (pixel & 0xff00) >> 8;
                rgbB = (pixel & 0xff);
                RGBTriple rgbTriple = new RGBTriple(rgbR,rgbG,rgbB);
                rgbTriples[i][j] = rgbTriple;
            }
        }
        /**取二值化屏幕类型*/
        RGBTriple[] palettes = getPalette(deviceTypeEnums);
        /**调用核心算法*/
        byte[][] to = floydSteinbergDither(rgbTriples,palettes);
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < to.length; i++) {
            for (int j = 0; j < to[i].length; j++) {
                if (to[i][j] == 0) {
                    // 处理黑色
                    bufferedImage.setRGB(i, j, 0);
                } else if (to[i][j] == 1) {
                    // 处理白色
                    bufferedImage.setRGB(i, j, (255 << 16) + (255 << 8) + 255);
                } else if (to[i][j] == 2 && deviceTypeEnums.getValue() == 1) {
                    // 处理红色
                    bufferedImage.setRGB(i, j, (255 << 16));
                } else if (to[i][j] == 2 && deviceTypeEnums.getValue() == 2) {
                    // 处理黄色
                    bufferedImage.setRGB(i, j, (255 << 16) + (255 << 8));
                }
            }
        }
        return bufferedImage;
    }

    /**
     * 取二值化屏幕类型
     *
     * @param deviceTypeEnums 屏幕类型
     * @return RGBTriple[]
     * @see RGBTriple
     */
    private static RGBTriple[] getPalette(DeviceTypeEnums deviceTypeEnums) {
        final RGBTriple[] palette;

        if (deviceTypeEnums.getValue() == 1) {
            //黑白红屏设备
            palette = new RGBTriple[]{
                    new RGBTriple(0, 0, 0),
                    new RGBTriple(255, 255, 255),
                    new RGBTriple(255, 0, 0)
            };

        } else if(deviceTypeEnums.getValue() == 2){
            //黑白黄屏设备
            palette = new RGBTriple[]{
                    new RGBTriple(0, 0, 0),
                    new RGBTriple(255, 255, 255),
                    new RGBTriple(255, 255, 0)
            };
        }else {
            //黑白屏设备
            palette = new RGBTriple[]{
                    new RGBTriple(0, 0, 0),
                    new RGBTriple(255, 255, 255)
            };
        }
        return palette;
    }

    /**
     * 核心算法，二值化处理
     *
     * @param image   图片
     * @param palette 屏幕类型
     * @return 二值化数组， 0表示黑，1表示白，2表示红或黄（假定红和黄色不同时存在）
     */
    private static byte[][] floydSteinbergDither(RGBTriple[][] image, RGBTriple[] palette){
        byte[][] result = new byte[image.length][image[0].length];

        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[y].length; x++) {
                RGBTriple currentPixel = image[y][x];
                byte index = findNearestColor(currentPixel, palette);
                result[y][x] = index;

                for (int i = 0; i < 3; i++)
                {
                    int error = (currentPixel.channels[i] & 0xff) - (palette[index].channels[i] & 0xff);
                    if (x + 1 < image[0].length) {
                        image[y+0][x+1].channels[i] =
                                plus_truncate_uchar(image[y+0][x+1].channels[i], (error*7) >> 4);
                    }
                    if (y + 1 < image.length) {
                        if (x - 1 > 0) {
                            image[y+1][x-1].channels[i] =
                                    plus_truncate_uchar(image[y+1][x-1].channels[i], (error*3) >> 4);
                        }
                        image[y+1][x+0].channels[i] =
                                plus_truncate_uchar(image[y+1][x+0].channels[i], (error*5) >> 4);
                        if (x + 1 < image[0].length) {
                            image[y+1][x+1].channels[i] =
                                    plus_truncate_uchar(image[y+1][x+1].channels[i], (error*1) >> 4);
                        }
                    }
                }
            }
        }
        return result;
    }

    private static byte plus_truncate_uchar(byte a, int b) {
        if ((a & 0xff) + b < 0) {
            return 0;
        } else if ((a & 0xff) + b > 255) {
            return (byte) 255;
        } else {
            return (byte) (a + b);
        }
    }

    private static byte findNearestColor(RGBTriple color, RGBTriple[] palette) {
        int minDistanceSquared = 255*255 + 255*255 + 255*255 + 1;
        byte bestIndex = 0;
        for (byte i = 0; i < palette.length; i++) {
            int Rdiff = (color.channels[0] & 0xff) - (palette[i].channels[0] & 0xff);
            int Gdiff = (color.channels[1] & 0xff) - (palette[i].channels[1] & 0xff);
            int Bdiff = (color.channels[2] & 0xff) - (palette[i].channels[2] & 0xff);
            int distanceSquared = Rdiff*Rdiff + Gdiff*Gdiff + Bdiff*Bdiff;
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}