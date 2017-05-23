package com.hz.qrscan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hz.qrscan.utils.BitmapUtil;
import com.hz.qrscan.utils.QRGenerateUtil;
import com.hz.qrscan.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 生成二维码
 */
public class GenerateActivity extends AppCompatActivity {

    EditText mEditText;
    ImageView mImageView;
    ImageView mCenterPic;
    Button mBtnSave;

    private Bitmap mBitmap;
    private Bitmap mQrcode;
    private String mDirPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        mEditText = (EditText) findViewById(R.id.et_text);
        mImageView = (ImageView) findViewById(R.id.iv_barcode);
        mCenterPic = (ImageView) findViewById(R.id.center_pic);
        mBtnSave = (Button) findViewById(R.id.bt_save);

        setTitle("生成二维码");
        //路径
        mDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QrCode";
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mCenterPic.setImageBitmap(mBitmap);
    }

    /**
     * 选择图片
     * @param v
     */
    public void onSelectPic(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    /**
     * 清空图片
     * @param v
     */
    public void centerPic(View v) {
        if (mBitmap != null) {
            mBitmap = null;
            mCenterPic.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 保存二维码
     * @param v
     */
    public void saveQrPic(View v) {
        if (mQrcode != null) {
            try {
                StringBuffer sb = new StringBuffer();
                sb.append(mDirPath).append("/qr").append(System.currentTimeMillis()).append(".jpg");
                String filePath = sb.toString();

                File f = new File(filePath);
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream out = new FileOutputStream(mDirPath);
                mQrcode.compress(Bitmap.CompressFormat.JPEG, 100,out);
                out.flush();
                out.close();
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(new File(filePath));
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);

                mBtnSave.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "图片已保存到：" + mDirPath, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成二维码
     * @param v
     */
    public void onGenerate(View v) {
        final String s = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(getApplication(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQrcode = QRGenerateUtil.createQRImage(s, 600, 600, mBitmap);
                if (mQrcode != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示图片
                            mImageView.setImageBitmap(mQrcode);
                            //显示保存按钮
                            mBtnSave.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 选择相册里面的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                //获取图片轮廓
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                //计算采样率
                int reqWidth = 100;
                int reqHeight = 100;
                int sampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight);
                //加载图片
                options.inSampleSize = sampleSize;
                options.inJustDecodeBounds = false;
                mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                mCenterPic.setImageBitmap(mBitmap);
                mCenterPic.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
