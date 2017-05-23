package com.hz.lib.webapi;

/**
 * WebAPI接口监听接口，提供给给客户端统一的操作规范。
 */
public interface WebAPIListener {

    /**
     * 开始加载，访问网络前被调用
     */
    void onStart();

    /**
     * 结束加载，在加载成功之前被调用
     */
    void onFinish();

    /**
     * 加载成功
     * @param result 服务器返回的数据
     */
    void onSuccess(String result);

    /**
     * 加载失败
     * @param errCode 失败代码
     * @param errMessage 失败信息
     */
    void onFail(int errCode, String errMessage);
}
