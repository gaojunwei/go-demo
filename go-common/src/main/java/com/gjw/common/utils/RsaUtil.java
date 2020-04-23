package com.gjw.common.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    public static final String KEY_ALGORITHM = "RSA";

    private static int rsaEncryptBlock = 244;

    private static int rsaDecryptBlock = 256;

    private static Map<String, String> keyMap = new HashMap<String, String>(1);

    public static void initRsa() throws NoSuchAlgorithmException {
        //初始化密钥算法
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //设定加密算法长度，比较常见的是使用1024、2048，其中1024目前已经不是绝对安全，长度的区别主要是加密性能会有区别
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        /**
         * 在实际的项目使用中，公钥和私钥可能会提取出来分别存放，私钥一般要求保密，普遍是加密存放在服务器的数据库或者存放在配置文件
         * 这里我们使用base64进行一个简单的转码，后续的函数再模拟传入公钥和私钥进行加解密
         */
        String publicStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateStr = Base64.encodeBase64String(privateKey.getEncoded());
        System.out.println("公钥:" + publicStr);
        System.out.println("私钥:" + privateStr);
        keyMap.put(publicStr, privateStr);
    }

    /**
     * 获取公钥
     *
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    private static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取私钥
     *
     * @param privateKeyStr
     * @return
     */
    private static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) {
        try {
            Base64 base64 = new Base64();
            byte[] buffer = base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥签名
     */
    public static byte[] sign(byte[] plainTextData) {
        try {
            Signature signature = Signature.getInstance("SHA256WithRSA");
            RSAPrivateKey privateKey = loadPrivateKeyByStr(keyMap.get(keyMap.keySet().toArray()[0]));
            signature.initSign(privateKey);
            signature.update(plainTextData);
            byte[] output = signature.sign();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     */
    public static byte[] decrypt(byte[] cipherData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            RSAPrivateKey privateKey = loadPrivateKeyByStr(keyMap.get(keyMap.keySet().toArray()[0]));
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] encryptedData = cipherData;
            int inputLen = encryptedData.length;
            for (int i = 0; inputLen - offSet > 0; offSet = i * rsaDecryptBlock) {
                byte[] cache;
                if (inputLen - offSet > rsaDecryptBlock) {
                    cache = cipher.doFinal(encryptedData, offSet, rsaDecryptBlock);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                ++i;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥验签
     *
     * @param cipherData    密文
     * @param signatureInfo 明文
     */
    public static boolean verify(byte[] cipherData, String signatureInfo) {
        try {
            Signature signature = Signature.getInstance("SHA256WithRSA");
            //这里的密钥我们直接使用了从map获取的方式，在项目运用中一般是把公钥的String传递进来
            RSAPublicKey publicKey = loadPublicKeyByStr(String.valueOf(keyMap.keySet().toArray()[0]));
            signature.initVerify(publicKey);
            signature.update(signatureInfo.getBytes());
            return signature.verify(cipherData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 公钥加密
     */
    public static byte[] encrypt(byte[] plainTextData) {
        try {
            //这里的密钥我们直接使用了从map获取的方式，在项目运用中一般是把公钥的String传递进来
            RSAPublicKey publicKey = loadPublicKeyByStr(String.valueOf(keyMap.keySet().toArray()[0]));
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            int inputLen = plainTextData.length;
            byte[] inputData = plainTextData;
            for (int i = 0; inputLen - offSet > 0; offSet = i * rsaEncryptBlock) {
                byte[] cache;
                if (inputLen - offSet > rsaEncryptBlock) {
                    cache = cipher.doFinal(inputData, offSet, rsaEncryptBlock);
                } else {
                    cache = cipher.doFinal(inputData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                ++i;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        initRsa();
        String plainTextData = "明文数据-哈哈哈";
        byte[] cipherData = encrypt(plainTextData.getBytes());
        System.out.println("数据使用公钥加密后:" + Base64.encodeBase64String(cipherData));
        System.out.println("数据使用私钥解密后:" + new String(decrypt(cipherData)));
        byte[] sign = sign(plainTextData.getBytes());
        System.out.println("数据使用私钥签名后:" + Base64.encodeBase64String(sign));
        System.out.println("数据使用公钥验签后:" + verify(sign, plainTextData));
    }
}