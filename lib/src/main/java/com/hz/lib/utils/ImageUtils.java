package com.hz.lib.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片工具类
 * 注意：此工具类下的某些获取图片方法会有可能造成OOM，谨慎使用。
 *
 * @author LiuPeng
 */
public class ImageUtils {

    private ImageUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 从URL获取Bitmap，注意在非UI线程调用，注意此方法加载较大图片可能会导致OOM
     * 推荐使用此方法的重载 Bitmap getBitmapFromUrl(String url, int maxWidth, int maxHeight)
     * @param url 远程图片地址
     * @return Bitmap 获取到的bitmap
     * @throws IOException
     */
    public static Bitmap getBitmapFromUrl(String url) throws IOException {
        URL realUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 从URL获取图片，尽量避免OOM，注意在非UI线程调用
     * @param url 图片连接
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 图片
     */
    public static Bitmap getBitmapFromUrl(String url, int maxWidth, int maxHeight) throws IOException{
        URL realUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            /*使用inSamleSize进行第一步压缩（按比例压缩）,inSmaleSize参数是整形值，代表缩放1/N^2*/
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, null, options);
        }
        return null;
    }


    /**
     * 从文件获取图片，注意此方法加载较大图片可能会导致OOM
     * 推荐使用此方法的重载 Bitmap getBitmapFromFile(String file, int maxWidth, int maxHeight)
     * @param file 文件名
     * @return bitmap
     */
    public static Bitmap getBitmapFromFile(String file) {
        return BitmapFactory.decodeFile(file);
    }

    /**
     * 从文件中获取图片，尽量避免OOM
     *
     * @param file 文件路径
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 图片
     */
    public static Bitmap getBitmapFromFile(String file, int maxWidth, int maxHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        /*使用inSamleSize进行第一步压缩（按比例压缩）,inSmaleSize参数是整形值，代表缩放1/N^2*/
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    /**
     * 从资源中获取图片，，注意此方法加载较大图片可能会导致OOM
     * 推荐使用此方法的重载 Bitmap getBitmapFromResource(Resources res, int resId, int width, int height)
     * @param res 资源对象
     * @param resId 资源id
     * @return 图片文件
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId){
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 从资源获取图片，尽量避免OOM
     *
     * @param res    资源
     * @param resId  资源id
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmapFromResource(Resources res, int resId, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        /*使用inSamleSize进行第一步压缩（按比例压缩）,inSmaleSize参数是整形值，代表缩放1/N^2*/
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 缩放Bitmap到置顶的分辨率
     * 按比例压缩公式参考： width, width * options.outHeight / options.outWidth);
     * @param srcBitmap 源bitmap
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return 图片
     */
    public static Bitmap scaleBitmap(Bitmap srcBitmap, int reqWidth, int reqHeight) {
        //filter决定放大图片是否平滑，这里是缩小图片，设置为false
        Bitmap newBitmap = Bitmap.createScaledBitmap(srcBitmap, reqWidth, reqHeight, false);
        //如果指定的宽度和高度与原图的相等，这个方法会直接返回原bitmap，所以要判断一下是否相等再将原bitmap释放
        if (srcBitmap != newBitmap) {
            srcBitmap.recycle();
            System.gc();
        }
        return newBitmap;
    }

    /**
     * 按质量压缩图片
     *
     * @param srcBitmap 原图片
     * @param quality   质量百分比
     * @return 新图片，原图片会被回收；压缩失败返回原图片
     */
    public static Bitmap compressImage(Bitmap srcBitmap, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        byte[] imgByte = out.toByteArray();
        try {
            out.flush();
            out.close();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            srcBitmap.recycle();
            srcBitmap = null;
            System.gc();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return srcBitmap;
    }

    /**
     * 保存图片到文件系统
     *
     * @param fileName 文件名
     * @param bitmap   图片
     * @return
     */
    public static void saveBitmapToFile(Bitmap bitmap, String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 图片转base64
     * @param bitmap 图片
     * @return base64字符串
     */
    public String Bitmap2Base64(Bitmap bitmap) {
        //压缩后储存在字节流里
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imgByte = baos.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    /**
     * 获取缩放比例
     *
     * @param options   图片参数
     * @param reqWidth  目标宽度
     * @param reqHeight 目标高度
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 从文件获取图片，压缩图片并保存到文件
     * @param srcPath  获取的图片路径
     * @param desPath  存入的图片路径
     */
    public static void compressPicture(String srcPath, String desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 1024f;//
        float ww = 1024f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = (float) (w / ww);
        } else if (w < h && h > hh) {
            be = (float) (h / hh);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
