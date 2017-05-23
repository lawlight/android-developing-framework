package com.hz.lib.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 */
public class EncryptionUtils {

    private EncryptionUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * MD5加密
     *
     * @param text    待加密文本
     * @param bit     加密位数，16或32
     * @param isUpper 是否是大写
     * @return 加密后文本，如果加密失败，返回null
     */
    public static String getMD5String(String text, int bit, boolean isUpper) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(text.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        String result = null;
        //16位加密，从第9位到25位
        if (bit == 16) {
            result = md5StrBuff.substring(8, 24).toString();
        } else {
            result = md5StrBuff.toString();
        }
        if (isUpper) {
            result = result.toUpperCase();
        } else {
            result = result.toLowerCase();
        }
        return result;
    }
}
