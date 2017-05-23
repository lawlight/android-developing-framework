package com.huazheng.umeng;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Map;


/**
 * 友盟社会化组件工具类
 * 使用需要在Application中调用：
 * {
     // 友盟社会化账号注册
     // 微信 appid appsecret
     PlatformConfig.setWeixin("wxd7beb01596bed0df", "9fd7592aaf513f751daeb5f1e9e4a0f7");
     // 新浪微博 appkey appsecret
     PlatformConfig.setSinaWeibo("135679294", "2df1e40111eaf9e575de0af5e0f55514");
     // QQ和Qzone appid appkey
     PlatformConfig.setQQZone("1105699075", "xm1pSV6Fz7uSqlxm");
     }
 * Created by LiuPeng on 16/8/24.
 */
public class ShareUtils {


    Activity activity;

    public ShareUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 第三方登录监听接口
     */
    public interface ThirdLoginListener{
        void onLoginComplete();
        void onInfoComplete(User user);
        void onError(String message);
        void onCancel(String message);
    }

    /**
     * 显示友盟默认的分享对话框（微信、朋友圈、新浪微博、qq、qq空间）
     * @param title 分享标题
     * @param text 分享内容
     * @param media 分享媒体（图片路径）
     * @param targetUrl 分享链接
     */
    public void showShareDialog(String title, String text, String media, String targetUrl, UMShareListener umShareListener){
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};

        ShareAction action = getShareAction(title, text, media, targetUrl, null, umShareListener);
        action.setDisplayList(displaylist);
        action.open();
    }

    /**
     * 获取分享action，使用share方法进行分享
     * @param title
     * @param text
     * @param media
     * @param targetUrl
     * @param platform
     */
    public ShareAction getShareAction(String title, String text, String media, String targetUrl, SHARE_MEDIA platform, UMShareListener umShareListener){

        if(text == null){
            text = "来自" + activity.getResources().getString(R.string.app_name) + "的分享";
        }
        UMImage image;
        if(!TextUtils.isEmpty(media)){
            image = new UMImage(activity, media);
        }else{
            image = new UMImage(activity, R.mipmap.share_icon);
        }
        image.setThumb(new UMImage(activity, R.mipmap.share_icon));

        UMWeb web = new UMWeb(targetUrl);
        web.setTitle(title);        //标题
        web.setThumb(image);        //缩略图
        web.setDescription(text);   //描述

        ShareAction action = new ShareAction(activity)
                .withText(text)
                .withMedia(web);
        if(platform != null){
            action.setPlatform(platform);
        }
        if(umShareListener != null){
            action.setCallback(umShareListener);
        }
        return action;
    }

    /**
     * 友盟第三方登录并获取用户信息，此方法将授权和获取信息两个操作合并成同一个，使用自定义的监听接口接收返回信息
     * @param context
     * @param platform 第三方平台，目前支持SHARE_MEDIA.QQ、SHARE_MEDIA.WEIXIN、SHARE_MEDIA.SINA
     * @param listener 登录监听，成功返回User对象，如果User对象内容有变化，需要修改User类和本方法的解析
     */
    public void thirdLogin(final Context context, final SHARE_MEDIA platform, final ThirdLoginListener listener){
        final UMShareAPI mShareAPI = UMShareAPI.get(context);
        if(!mShareAPI.isInstall((Activity) context, platform)){
            listener.onError("未安装应用");
            return;
        }
        //授权
        mShareAPI.doOauthVerify((Activity) context, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                listener.onLoginComplete();
                Log.e("debug","第三方登录成功,openid:"+User.get().openid+"，昵称："+User.get().nickname+"，头像:"+User.get().avatar);
                //获取用户信息
                mShareAPI.getPlatformInfo((Activity) context, share_media, new UMAuthListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        User user = User.get();
                        if(share_media == SHARE_MEDIA.QQ){
                            user.from = "qq";
                            user.openid = map.get("openid");
                            user.avatar = map.get("profile_image_url");
                            user.nickname = map.get("screen_name");
                        } else if(share_media == SHARE_MEDIA.WEIXIN){
                            user.from = "wapweixin";
                            user.openid = map.get("openid");
                            user.unionid = map.get("unionid");
                            user.nickname = map.get("nickname");
                            user.avatar = map.get("headimgurl");
                        } else if(share_media == SHARE_MEDIA.SINA){
                            user.from = "SINA";
                            user.openid = map.get("uid");
                            user.avatar = map.get("iconurl");
                            user.nickname = map.get("name");
                        }
                        Log.e("debug","第三方信息获取成功,openid:"+User.get().openid+"，昵称："+User.get().nickname+"，头像:"+User.get().avatar);
                        listener.onInfoComplete(user);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        listener.onError("获取信息失败：" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        listener.onCancel("获取用户信息取消");
                    }
                });
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                listener.onError("授权失败：" + t.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                listener.onCancel("授权取消");
            }
        });
    }
}
