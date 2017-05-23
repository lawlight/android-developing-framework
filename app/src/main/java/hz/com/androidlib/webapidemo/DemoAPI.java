package hz.com.androidlib.webapidemo;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.hz.lib.webapi.WebAPI;

import java.util.HashMap;

/**
 * Created by LiuPeng on 16/6/12.
 */
public abstract class DemoAPI extends WebAPI {
    /**
     * 构造方法
     *
     * @param context
     */
    public DemoAPI(Context context) {
        super(context);
    }

    @Override
    protected void getParams(HashMap<String, Object> params) {
        params.put("token", "123456");
    }

    @Override
    protected String preHandle(String result) throws JSONException {
        //{"code":200,"data":"数据"}
        if(result == null){
            sendMessage(ERROR, "数据错误");
            return null;
        }
        return "数据";
    }
}
