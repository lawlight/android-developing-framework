package com.hz.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * 相机工具类
 * Created by LiuPeng on 16/9/13.
 */
public class CameraUtils {

    private Context context;
    private Object source;

    //图片地址
    private String imageFile;
    //类型，相机或相册
    private int type;

    private int requestCode;

    /**
     * 构造方法
     * @param context
     * @param source 传onActivityResult所在的Activity或Fragment(v4)
     */
    public CameraUtils(Context context, Object source) {
        this.context = context;
        this.source = source;
    }

    /**
     * 打开相机或相册
     * @param type 1-相机 2-相册
     * @param path 保存图片路径，传null会创建临时缓存路径
     * @param requestCode 用于区别界面中多个地方调用相机
     */
    public void startCameraOrAlbumActivity(int type, String path, int requestCode){
        this.requestCode = requestCode;
        this.type = type;
        Intent intent = new Intent();
        if(type == 1){
            //相机
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir;
            File file;
            if(path == null){
                dir = context.getExternalCacheDir();
                file = new File(dir, "photoCache_" + new Date().getTime() + ".jpg");
                imageFile = file.getAbsolutePath();
            }else{
                file = new File(path);
                dir = file.getParentFile();
                imageFile = path;
            }
            if(!dir.exists()){
                dir.mkdirs();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        }else if(type == 2){
            //相册
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
        }
        if(source instanceof Activity){
            ((Activity) source).startActivityForResult(intent, requestCode);
        }else if(source instanceof Fragment){
            ((Fragment) source).startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取图片，在onActivityResult中调用，把参数直接传过来
     *
     */
    public String onActivityResult(int requestCode, int resultCode, Intent data){
        Log.e("utils", "相机相册回调");
        if(resultCode != Activity.RESULT_OK || this.requestCode != requestCode){
            return null;
        }

        if(data == null && type == 1){
            //由于相机是指定了路径，不会返回缩略图，data为null，此时直接返回图片地址
            return imageFile;
        }else{
            //相册直接返回uri中的路径即可
            Uri uri = data.getData();
            if(uri == null){
                return null;
            }else{
                return uri.getPath();
            }
        }
    }
}
