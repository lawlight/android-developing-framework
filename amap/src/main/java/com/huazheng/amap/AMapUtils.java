package com.huazheng.amap;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 高德定位工具类
 * Created by LiuPeng on 2017/2/17.
 */

public class AMapUtils {

    private Context mContext;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption;


    public AMapUtils(Context context){
        mContext = context;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);

        //初始化定位配置
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //请求超时时间 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

    }

    /**
     * 开启定位
     * @param locationListener
     *
     * 示例：
     * startLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
            //可在其中解析amapLocation获取相应内容。
            }else {
            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError","location Error, ErrCode:"
            + aMapLocation.getErrorCode() + ", errInfo:"
            + aMapLocation.getErrorInfo());
            }
            }
            }
            });
     */
    public void startLocation(AMapLocationListener locationListener){
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation(){
        mLocationClient.stopLocation();
    }

    /**
     * 销毁定位
     */
    public void onDestroy(){
        mLocationClient.onDestroy();
    }

    public AMapLocationClient getLocationClient() {
        return mLocationClient;
    }

    public AMapLocationClientOption getLocationOption() {
        return mLocationOption;
    }
}
