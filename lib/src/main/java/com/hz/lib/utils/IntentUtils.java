package com.hz.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Intent工具类，注意使用时声明相关的权限。
 *
 * @author LiuPeng
 */
public class IntentUtils {

    private IntentUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 显示网页
     *
     * @param url 网页url
     */
    public static void showWebPage(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 显示地图
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    public static void showMap(Context context, double longitude, double latitude) {
        Uri uri = Uri.parse("geo:" + longitude + "," + latitude);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 拨打电话（直接拨打）<br/>
     * 需要android.permission.CALL_PHONE权限
     *
     * @param telNum 电话号码
     */
    public static void makePhoneCall(Context context, String telNum) {
        Uri uri = Uri.parse("tel:" + telNum);
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(it);
    }

    /**
     * 调用拨号程序
     *
     * @param telNum 电话号码
     */
    public static void showPhoneCall(Context context, String telNum) {
        Uri uri = Uri.parse("tel:" + telNum);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 发送短信（打开短信应用）
     *
     * @param text 短信内容
     */
    public static void sendSMS(Context context, String text) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.putExtra("sms_body", text);
        it.setType("vnd.android-dir/mms-sms");
        context.startActivity(it);
    }

    /**
     * 发送短信
     *
     * @param receiver 收信人
     * @param text     短信内容
     */
    public static void sendSMS(Context context, String receiver, String text) {
        Uri uri = Uri.parse("smsto:" + receiver);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", text);
        context.startActivity(it);
    }

    /**
     * 发送电子邮件（调用系统邮件应用）
     *
     * @param reciver 收件人
     * @param subject 主题
     * @param body    正文
     */
    public static void sendEMail(Context context, String reciver, String subject, String body) {
        String[] recivers = new String[]{reciver};
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(Intent.EXTRA_EMAIL, recivers);
        myIntent.putExtra(Intent.EXTRA_CC, "");
        myIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        myIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(myIntent);
    }

    /**
     * 显示联系人应用
     */
    public static void showContacts(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        context.startActivity(intent);
    }

}
