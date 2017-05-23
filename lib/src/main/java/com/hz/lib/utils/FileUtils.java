package com.hz.lib.utils;

import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author LiuPeng
 */
public class FileUtils {

    private FileUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 将文件转换为base64字符串
     *
     * @param filePath 文件路径
     * @return
     */
    public static String file2Base64(String filePath) {
        File file = new File(filePath);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 文件转为base64并输出到文件
     *
     * @param filePath
     * @return
     */
    public static void file2Base64File(String filePath, String toFile) {
        String base64 = file2Base64(filePath);
        Log.d("base64", base64);
        try {
            FileOutputStream outputStream = new FileOutputStream(toFile);
            outputStream.write(base64.getBytes("utf-8"));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
