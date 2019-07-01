package com.jd.innovation.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 常用  编码/解码  工具类
 *
 * @author gjw
 * @create 2017-12-02 20:33
 **/
public class EncryptionUtils {
    /********************************获取字节数组********************************/
    /**
     * 得到文件的字节数组
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static byte[] getFileByte(String path) throws Exception {
        return getFileBytes(new File(path));
    }
    /**
     * 获取文件的字节数组
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getFileBytes(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        return getFileBytes(fileInputStream);
    }
    /**
     * 获取输入流的字节数组
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] getFileBytes(InputStream inputStream) throws Exception {
        return IOUtils.toByteArray(inputStream);
    }

    /********************************Base64编码********************************/
    /**
     * 获取文件的base64字符串
     * @param file
     * @return
     * @throws Exception
     */
    public static String base64Encode(File file) throws Exception {
        byte[] buffer = EncryptionUtils.getFileBytes(file);
        return base64Encode(buffer);
    }
    /**
     * 获取字符串的base64编码
     * @param str
     * @return
     */
    public static String base64Encode(String str) {
        return base64Encode(str.getBytes());
    }
    /**
     * （核心base64编码）获取字节数组的base64编码
     * @param inputStream
     * @return
     */
    public static String base64Encode(InputStream inputStream) throws Exception {
        return base64Encode(getFileBytes(inputStream));
    }
    /**
     * （核心base64编码）获取字节数组的base64编码
     * @param bytes
     * @return
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /********************************Base64解码********************************/
    /**
     * base64解码
     * @param base64Str
     * @return
     */
    public static byte[] base64Decode(String base64Str) {
        return base64Decode(base64Str.getBytes());
    }
    /**
     * base64解码
     * @param base64File base64编码文件
     * @return
     */
    public static byte[] base64Decode(File base64File) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(base64File);
        return base64Decode(fileInputStream);
    }
    /**
     * base64解码
     * @param inputStream base64输入流
     * @return
     */
    public static byte[] base64Decode(InputStream inputStream) throws Exception {
        return base64Decode(getFileBytes(inputStream));
    }
    /**
     * （核心base64解码）base64解码
     * @param bytes
     * @return
     */
    public static byte[] base64Decode(byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /********************************加密算法********************************/
    /**
     * 获取字符串的MD5值
     * @param str
     * @return
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }
    /**
     * 获取字符串的MD5值
     * @param file
     * @return
     */
    public static String md5(File file) throws Exception {
        return DigestUtils.md5Hex(getFileBytes(file));
    }

    /**
     * 获取字符串的SHA1Hex值
     * @param str
     * @return
     */
    public static String sha1Hex(String str) {
        return DigestUtils.sha1Hex(str);
    }

    /**
     * 获取字符串的sha256Hex值
     * @param str
     * @return
     */
    public static String sha256Hex(String str) {
        return DigestUtils.sha256Hex(str);
    }
}
