package com.huazheng.umeng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户实体类
 * Created by LiuPeng on 16/6/27.
 */
public class User {

    private static User mUser;

    public static User get(){
        if(mUser == null){
            mUser = new User();
        }
        return mUser;
    }

    /**
     * 加载本地保存的用户信息
     * @param context
     */
    public void load(Context context){
        SharedPreferences sp = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        //uid = sp.getString("uid", null);
        mobile = sp.getString("mobile", null);
        nickname = sp.getString("nickname", null);
        password = sp.getString("password", null);
        avatar = sp.getString("avatar", null);
        openid = sp.getString("openid", null);
        unionid = sp.getString("unionid", null);
        from = sp.getString("from", null);
    }

    /**
     * 将用户信息保存到本地
     * @param context
     */
    public void save(Context context){
        SharedPreferences sp = context.getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //editor.putString("uid", uid);
        editor.putString("mobile", mobile);
        editor.putString("nickname", nickname);
        editor.putString("password", password);
        editor.putString("avatar", avatar);
        editor.putString("openid", openid);
        editor.putString("unionid", unionid);
        editor.putString("from", from);
        editor.commit();
    }

    /**
     * 登出
     * @param context
     */
    public void logout(Context context){
        this.uid = null;
        this.password = null;
        nickname = null;
        avatar = null;
        openid = null;
        unionid = null;
        from = null;
        save(context);
    }

    public String uid;      //用户id
    public String mobile;   //手机号
    public String nickname; //用户名
    public String avatar;   //头像

    //额外参数：
    public String password; //密码
    public String openid;   //第三方openid
    public String unionid;  //微信登录额外多一个unionid
    public String from;     //是哪个第三方平台QQ、WEIXIN、SINA，普通登录为null

    //登录后获取的token
    public String token;

    /**
     * 判断是否登录
     * @return
     */
    public  boolean isLogin(){
        return !(uid == null);
    }
}
