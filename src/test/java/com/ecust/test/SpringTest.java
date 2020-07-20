package com.ecust.test;

import com.jdjr.crawler.tcpj.Application;
import com.jdjr.crawler.tcpj.common.util.Arith;
import com.jdjr.crawler.tcpj.common.util.UuidUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 类描述
 *
 * @Author gaojunwei
 * @Date 2020/7/17 14:16
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class SpringTest {

    public static byte[] toByteArray3(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片下载，拼接文字，返回base64字符
     *

     * @return
     */
    @Test
    public void tet(){
        getImgBase64(null,"请点击圆柱体上面的字母");
    }

    private String getImgBase64(String imgSrc, String imgDescStr) {
        //图片下载
        String base64Str = null;
        try {
            byte[] imgBts = toByteArray3("D:\\proxy\\imgCode\\a.jpg");
            if (imgBts == null || imgBts.length == 0) {
                return base64Str;
            }
            InputStream inputStream = new ByteArrayInputStream(imgBts);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            //图片进行缩放
            BufferedImage scaleImg = getScaleImg(bufferedImage);

            //获取图片的高度和宽度
            int width = scaleImg.getWidth();
            int height = scaleImg.getHeight();

            //设置文字图片信息
            int textHeight = 30;
            BufferedImage textImage = new BufferedImage(width, textHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics graphics = textImage.getGraphics();
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.black);
            Font font = new Font("宋体", Font.BOLD, 18);
            graphics.setFont(font);
            graphics.drawString(imgDescStr, 1, 20);

            int[] textImageArray = new int[width * textHeight];
            textImageArray = textImage.getRGB(0, 0, width, textHeight, textImageArray, 0, width);

            /**生成新图片*/
            int[] bufferedImageArray = new int[width * height];
            bufferedImageArray = scaleImg.getRGB(0, 0, width, height, bufferedImageArray, 0, width);


            /**合并成新图片*/
            BufferedImage imageNew = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
            //设置上半部分的RGB
            imageNew.setRGB(0, 0, width, height, bufferedImageArray, 0, width);
            //设置下半部分的RGB
            imageNew.setRGB(0, height, width, textHeight, textImageArray, 0, width);


            //获取图片base64字符串
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(imageNew, "jpg", stream);
            base64Str = new String(Base64.encodeBase64(stream.toByteArray()), "utf-8");

            try {
                File outDe = new File("D:/proxy/imgCode/");
                if (!outDe.exists())
                    outDe.mkdirs();
                //保存到本地
                File outFile = new File("D:/proxy/imgCode/" + UuidUtils.getUUID() + ".jpg");
                ImageIO.write(imageNew, "jpg", outFile);// 写图片
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Str;
    }
    /**
     * 按比例对图片进行缩放.
     */
    private BufferedImage getScaleImg(BufferedImage img) {
        //获取缩放后的长和宽
        int width = img.getWidth();
        int height = img.getHeight();
        double rate = Arith.div(Integer.toString(340), Integer.toString(width));
        int showHeight = (int) (height * rate);

        //获取缩放后的Image对象
        Image imgSrcScaled = img.getScaledInstance(340, showHeight, Image.SCALE_DEFAULT);
        //新建一个和Image对象相同大小的画布
        BufferedImage scaledImg = new BufferedImage(340, showHeight, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D graphics = scaledImg.createGraphics();
        //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
        graphics.drawImage(imgSrcScaled, 0, 0, null);
        //释放资源
        graphics.dispose();
        return scaledImg;
    }

    /**
     * 获取图片数据
     */
    private byte[] downLoadImgData(String url) {
        byte[] result = null;
        int reTryCount = 0;
        while (reTryCount < 2) {
            HttpGet httpGet = new HttpGet(url);
            //设置代理及超时配置
            CloseableHttpClient httpClient = getHttpClient(httpGet, 5000, 5000);

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    break;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                    }
                }
            }
            reTryCount++;
        }

        return result;
    }

    /**
     * 代理设置处理
     *
     * @return
     */
    private CloseableHttpClient getHttpClient(HttpRequestBase httpRequestBase, Integer conTime, Integer socktTime) {
        return HttpClients.createDefault();
    }
}
