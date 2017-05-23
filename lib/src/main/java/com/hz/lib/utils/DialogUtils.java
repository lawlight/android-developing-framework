package com.hz.lib.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * 对话框通用类，为应对今后对对话框有特殊要求的修改，将所有调用系统对话框的方法封装
 * 此类为单例类，需使用getInstance方法获取实例
 */
public class DialogUtils {

    private Context mContext;
    private static DialogUtils dialogUtil;
    private ProgressDialog mProgressDialog;
    private Toast toast;

    private DialogUtils(){}

    public static DialogUtils getInstance(Context context) {
        if (dialogUtil == null) {
            dialogUtil = new DialogUtils();
        }
        dialogUtil.mContext = context;
        return dialogUtil;
    }

    /**
     * 显示短时间的Toast
     *
     * @param text 文本
     */
    public void showShortToast(String text) {
        if (mContext == null) {
            return;
        }
        if(toast == null){
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 显示短时间的Snackbar
     * @param view 根视图
     * @param text 文本
     */
    public void showShortSnackbar(View view, String text){
        if(mContext == null){
            return;
        }
        Snackbar.make(view, text,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示简单的确认对话框，按钮无回调
     *
     * @param title 标题
     * @param text  文本
     */
    public void showCommitDialog(String title, String text) {
        if (mContext == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(text).setPositiveButton("确定", null).show();
    }

    /**
     * 显示简单的确认对话框，按钮点击后关闭activity
     *
     * @param title 标题
     * @param text  文本
     */
    public void showCommitDialogWithFinish(String title, String text) {
        if (mContext == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(text).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }
        }).show();
    }

    /**
     * 显示进度对话框
     *
     * @param title      标题
     * @param message    消息
     * @param cancelable 是否可以取消
     */
    public void showProgressDialog(String title, String message, boolean cancelable) {
        if (mContext == null) {
            return;
        }
        mProgressDialog = ProgressDialog.show(mContext, title, message);
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setCanceledOnTouchOutside(cancelable);
    }

    /**
     * 关闭进度对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
