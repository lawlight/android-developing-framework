package com.hz.lib.webapi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.myksoap2.SoapEnvelope;
import org.myksoap2.serialization.SoapObject;
import org.myksoap2.serialization.SoapSerializationEnvelope;
import org.myksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * 访问网络接口抽象类，使用okHttp实现网络访问<br />
 * 使用时可以先定义一个继承此类的抽象类A，在抽象类中：<br />
 * 重写getParams方法实现全局输入参数的赋值，<br />
 * 重写preHandle来实现返回结果的预处理，如果出现错误，方法返回null，并调用sendMessage发送错误信息<br />
 * <br />
 * 使用具体接口，定义接口类，继承抽象类A，再实现其他方法：<br />
 * 重写getParams设置入参，必要时需要使用URLEncoder，如果参数中有File类型value，则使用form表单的文件上传模式
 * 重写getURL设置访问的URL
 * 重写analysisOutput解析输出，如果解析出现错误，调用sendMessage方法并返回false
 * <br />
 * 之后就可以使用具体接口类B中的doHttpGet等方法进行网络访问
 */
public abstract class WebAPI {

    //OKHttpClient单例
    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    //标记当前访问队列的tag，用于可以取消任务
    private long callTag;

    //debug tag
    private static final String TAG = "WebAPI";

    //错误码
    //成功
    public static final int SUCCESS = 1;
    //错误
    public static final int ERROR = -1;

    /**
     * soap协议版本设置
     */
    public static final int SOAP_VERSION_10 = SoapEnvelope.VER10;
    public static final int SOAP_VERSION_11 = SoapEnvelope.VER11;
    public static final int SOAP_VERSION_12 = SoapEnvelope.VER12;

    //默认12版本
    private int SoapVersion = SoapEnvelope.VER12;

    //是否是.NET环境的WS
    private boolean dotNet = true;

    /**
     * 设置协议版本
     * @param soapVersion
     */
    public void setSoapVersion(int soapVersion) {
        SoapVersion = soapVersion;
    }

    /**
     * 设置是否为.NET WebService
     * @param dotNet
     */
    public void setDotNet(boolean dotNet) {
        this.dotNet = dotNet;
    }

    //接口监听
    private WebAPIListener listener;

    /**
     * 设置监听
     * @param listener
     */
    public void setListener(WebAPIListener listener) {
        this.listener = listener;
    }

    Context context;

    //记录当前的操作模式
    private String connectMode;

    /**
     * 获取当前链接的模式
     * @return POST、PUT、GET、DELETE、WS之一
     */
    public String getConnectMode() {
        return connectMode;
    }

    //超时时间，默认10秒
    private int timeOut = 10;

    /**
     * 设置链接超时时间
     * @param timeOut 超时时间（秒）
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 获取Context
     *
     * @return
     */
    public Context getContext() {
        return context;
    }

    //入参
    HashMap<String, Object> params = new HashMap<>();

    //接口控制
    private boolean canceled = false;
    private boolean debug = true;
    private boolean test = false;

    /**
     * 构造方法
     *
     * @param context
     */
    public WebAPI(Context context) {
        this.context = context;
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
            mOkHttpClient.setConnectTimeout(timeOut, TimeUnit.SECONDS);
        }
        //生成callTag;
        callTag = new Date().getTime();
    }

    /**
     * 设置是否是调试模式，调试模式下，输出调试log
     *
     * @param debug 是否是调试模式
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * 设置是否是测试模式，测试模式下，不会访问网络，会直接调用解析方法analysisOutput
     *
     * @param test
     */
    public void setTest(boolean test) {
        this.test = test;
    }

    /**
     * 取消任务
     *
     */
    public void cancel() {
        this.canceled = true;
        mOkHttpClient.cancel(callTag);
    }

    /**
     * 获取URL
     *
     * @return URL
     */
    abstract protected String getURL();

    /**
     * 获取WS的命名空间，在WS的父类中重写即可
     * @return
     */
    abstract protected String getNAMESPACE();

    /**
     * 获取WS的方法名，在WS的子类中重写，Http的父类中填默认值
     * @return
     */
    abstract protected String getMethodName();

    /**
     * 获取入参，如果传了文件类型，则使用FORM形式上传
     *
     * @param params 将接口入参直接put到这个map对象即可，如：
     *               params.put("name", "John");
     */
    protected abstract void getParams(HashMap<String, Object> params);

    /**
     * 执行Http Post请求
     * @param listener 接口监听器
     */
    public void doHttpPost(WebAPIListener listener) {
        this.listener = listener;
        doConnectInBackground("POST");
    }

    /**
     * 执行Http put异步请求
     * @param listener 接口监听器
     */
    public void doHttpPut(WebAPIListener listener) {
        this.listener = listener;
        doConnectInBackground("PUT");
    }

    /**
     * 执行Http delete异步请求
     * @param listener 接口监听器
     */
    public void doHttpDelete(WebAPIListener listener) {
        this.listener = listener;
        doConnectInBackground("DELETE");
    }

    /**
     * 执行Http get异步请求
     * @param listener 接口监听器
     */
    public void doHttpGet(WebAPIListener listener) {
        this.listener = listener;
        doConnectInBackground("GET");
    }

    /**
     * 执行http WebService异步请求
     * @param listener
     */
    public void doWebService(WebAPIListener listener){
        this.listener = listener;
        doConnectInBackground("WS");
    }

    /**
     * 开始WebAPI异步连接
     *
     * @param mode 访问模式，post或get
     * @return 连接是否成功
     */
    private void doConnectInBackground(String mode) {
        connectMode = mode;
        //开始
        if (listener != null) {
            listener.onStart();
        }
        //获取自定义入参
        params.clear();
        getParams(params);
        //撤销取消状态
        canceled = false;
        //测试模式，直接调用解析方法，模拟500毫秒网络访问
        if (test) {
            log("测试模式");
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (analysisOutput("")) {
                            sendMessage(SUCCESS, "");
                        }
                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return;
        }

        //WebService
        if(mode.equals("WS")){
            doWebServiceConnect();
        }else{
            doHttpConnect(mode);
        }
    }

    /**
     * 执行http请求
     * @param mode
     */
    private void doHttpConnect(String mode){
        //新的okHttp请求
        final Request request = getRequest(mode);
        log(request.toString());
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendMessage(ERROR, "网络异常，请检查网络连接");
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        String result = response.body().string();
                        log(request.urlString() + "返回结果：" + result);
                        //预处理
                        result = preHandle(result);
                        if (result != null) {
                            if (analysisOutput(result)) {
                                sendMessage(SUCCESS, result);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendMessage(ERROR, "无法解析返回数据");
                    }
                } else {
                    String result = response.body().string();
                    log(getURL() + "返回结果：" + result);
                    sendMessage(ERROR, result);
                }
            }
        });
    }

    /**
     * 执行webService请求
     */
    private void doWebServiceConnect(){
        new Thread(){
            @Override
            public void run(){
                try {
                    log("开始访问：" + getURL() + "，方法：" + getMethodName());
                    //遍历参数
                    SoapObject rpc = new SoapObject(getNAMESPACE(), getMethodName());
                    //参数赋值
                    Iterator iter = params.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = entry.getKey().toString();
                        if(entry.getValue() != null){
                            String val = entry.getValue().toString();
                            rpc.addProperty(key, entry.getValue());
                            log("参数：" + key + "=" + val);
                        }
                    }
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapVersion);
                    envelope.dotNet = dotNet;
                    envelope.bodyOut = rpc;
                    log(rpc.toString());
                    //输出到文件
                    //FileUtils.saveFile(rpc.toString());
                    envelope.setOutputSoapObject(rpc);
                    HttpTransportSE ht = new HttpTransportSE(getURL(), timeOut * 1000);
                    ht.debug = true;
                    //SOAP协议输出
//                    byte[] bytes = createRequestData(envelope,"UTF-8");
//                    String str = new String(bytes,"UTF-8");
                    ht.call(getNAMESPACE() + getMethodName(), envelope);
                    SoapObject detail = (SoapObject) envelope.bodyIn;
                    if(detail != null){
                        String result = detail.getProperty(0).toString();
                        log(getMethodName()+"的返回结果：" + result);
                        //预处理
                        result = preHandle(result);
                        if(result != null){
                            //分析接口返回的JSON方法
                            if(analysisOutput(result)){
                                sendMessage(SUCCESS, result);
                            }
                        }
                    } else{
                        sendMessage(ERROR,"网络异常，请检查网络连接");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(ERROR, "网络IO异常");
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendMessage(ERROR, "JSON解析数据异常");
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    sendMessage(ERROR, "服务器返回错误信息，请联系管理员");
                }
            }
        }.start();
    }

    /**
     * 获取请求
     * @return 返回相应请求
     */
    private Request getRequest(String mode){
        //是否传了文件
        boolean fileMode = isFileMode();

        String url = getURL();
        String paramsStr = "";
        log(mode + "访问：" + url);

        //定义请求体RequestBody
        RequestBody requestBody;

        //正式遍历参数
        Iterator iter = params.entrySet().iterator();
        if(!fileMode){
            //普通模式
            FormEncodingBuilder formBody = new FormEncodingBuilder();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                if(entry.getValue() == null){
                    continue;
                }
                String val = entry.getValue().toString();
                formBody.add(key, val);
                paramsStr += key + "=" + val + (iter.hasNext() ? "&" : "");
                log("参数：" + key + "=" + val);
            }
            //为避免参数为空报错，传默认参数
            formBody.add("app", "android");
            requestBody = formBody.build();
        }else{
            //文件上传模式
            MultipartBuilder multipartBuilder = new MultipartBuilder();
            multipartBuilder.type(MultipartBuilder.FORM);
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                Object val = entry.getValue();
                if(val == null){
                    continue;
                }
                if(val instanceof File){
                    //文件字段
                    File file = new File(val.toString());
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    multipartBuilder.addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\"" + key + "\"; filename =\"" + file.getName()+"\""), fileBody);
                } else {
                    //普通字段
                    multipartBuilder.addPart(Headers.of(
                            "Content-Disposition",
                            "form-data; name=\""+key+"\""),
                            RequestBody.create(null, val.toString()));
                }
                log("参数：" + key + "=" + val.toString());
            }
            requestBody = multipartBuilder.build();
        }

        //构造请求
        Request.Builder builder = new Request.Builder();
        builder.tag(callTag);
        if (mode.equals("POST")) {
            //POST请求
            builder.post(requestBody);
        } else if(mode.equals("GET")){
            //GET请求
            if(!"".equals(paramsStr)){
                if(url.contains("?")){
                    url = url + "&" + paramsStr;
                }else {
                    url = url + "?" + paramsStr;
                }
            }
        } else if(mode.equals("PUT")){
            //PUT请求
            builder.put(requestBody);
        } else if(mode.equals("DELETE")){
            //DELETE请求
            builder.delete(requestBody);
        }
        builder.url(url);
        return builder.build();
    }

    /**
     * 是否有传入文件
     * @return 是否文件模式
     */
    private boolean isFileMode(){
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if(entry.getValue() instanceof File){
                return true;
            }
        }
        return false;
    }

    /**
     * 预处理，不重写此方法的话，会原样返回接口的结果值result
     * 如果预处理中需要返回错误信息，则调用sendMessage输出错误信息，同时返回值为null
     *
     * @return 如果是错误状态，返回值应为null，正常则返回解析后的字符串。
     */
    protected String preHandle(String result) throws JSONException {
        return result;
    }

    /**
     * @param result 返回的字符串
     * @return 解析结果
     * @throws JSONException
     */
    abstract protected boolean analysisOutput(String result) throws JSONException;

    /**
     * 发送Handler消息
     *
     * @param what 连接状态code
     * @param obj  对象参数
     * @return 连接状态code
     */
    public void sendMessage(int what, Object obj) {
        //如果没有被取消，发送消息，否则什么也不做。
        if (!canceled) {
            handler.obtainMessage(what, obj).sendToTarget();
        }
    }

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //失败，先取消任务
            if(msg.what == ERROR){
                cancel();
            }
            //没有定义监听，返回
            if (listener == null) {
                return;
            }
            if (msg.what == SUCCESS) {
                listener.onSuccess(msg.obj.toString());
            } else {
                listener.onFail(msg.what, msg.obj.toString());
            }
            //结束接口访问
            listener.onFinish();
        }
    };

    /**
     * 输出日志
     *
     * @param text 日志内容
     */
    protected void log(String text) {
        if (debug) {
            Log.e(TAG, text);
        }
    }

    /**
     * 设置密钥和安全证书，如果有https验证需要，在Application的onCreate中调用此方法设置证书
     *
     * @param bksKey 密钥bks输入流，可传null，只实现https单向验证
     * @param password 密码，如果bks不为null，则password必须有值
     * @param certificates 单向验证的证书输入流，可将证书放置到asset中，使用getAssets().open("xxx.cer")来获取输入流
     *                     也可将证书内容赋值给字符串，使用new Buffer().writeUtf8(CER_XXX).inputStream()来获取输入流
     */
    public static void setCertificates(InputStream bksKey, String password, InputStream... certificates) {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //如果bks不为空，则需要添加密钥
            if(bksKey != null){
                if(password == null){
                    throw new UnsupportedOperationException("密钥的密码为空");
                }
                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksKey, password.toCharArray());

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(clientKeyStore, password.toCharArray());

                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            }else{
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            }
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
