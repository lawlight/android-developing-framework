package com.hz.lib.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.hz.lib.R;
import com.hz.lib.base.KV;
import com.hz.lib.utils.DialogUtils;
import com.hz.lib.webapi.WebAPIListener;

import java.util.List;


/**
 * 省市区联动View
 */
public class ProvinceView extends LinearLayout {

    /**
     * 数据接口
     */
    public interface IGetDataAPI{
        /**
         * 链接方法
         * @param listener 网络监听接口
         */
        void doConnect(WebAPIListener listener);

        /**
         * 获取省市区列表
         * @return
         */
        List<KV> getList();

        /**
         * 设置省或市id
         * @param id
         */
        void setId(String id);
    }

    public ProvinceView(Context context) {
        super(context);
        initView(context);
    }

    public ProvinceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public interface OnLoadListener{
        void onLoading();
        void onFinishLoad();
    }
    private OnLoadListener onLoadListener;

    /**
     * 设置加载监听
     * @param onLoadListener
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    Spinner spnProvince;
    Spinner spnCity;
    Spinner spnCounty;

    Context context;

    //省市区接口
    private IGetDataAPI provinceAPI;
    private IGetDataAPI cityAPI;
    private IGetDataAPI countyAPI;

    //adapter
    private AreaAdapter provinceAdapter;
    private AreaAdapter cityAdapter;
    private AreaAdapter countyAdapter;

    //标识是否加载完成
    private boolean finish;

    //选择的省市区
    private String province;
    private String city;
    private String county;

    /**
     * 获取省
     * @return
     */
    public KV getProvince(){
        return finish ? (KV) spnProvince.getSelectedItem() : null;
    }

    /**
     * 获取市
     * @return
     */
    public KV getCity(){
        return finish ? (KV) spnCity.getSelectedItem() : null;
    }

    /**
     * 获取区
     * @return
     */
    public KV getCounty(){
        return finish ? (KV) spnCounty.getSelectedItem() : null;
    }

    /**
     * 将最后一级区返回，可以继续传递联动效果
     * @return
     */
    public Spinner getSpnCounty() {
        return spnCounty;
    }

    /**
     * 初始化数据
     * @param provinceAPI 省接口
     * @param cityAPI 市接口
     * @param countyAPI 区接口
     * @param provinceId 省id，无默认值传null
     * @param cityId 市id，无默认值传null
     * @param countyId 区id，无默认值传null
     */
    public void initData(IGetDataAPI provinceAPI, IGetDataAPI cityAPI, IGetDataAPI countyAPI, String provinceId, String cityId, String countyId){

        //赋默认值
        province = provinceId;
        city = cityId;
        county = countyId;

        //加载省市区数据
        this.provinceAPI = provinceAPI;
        this.cityAPI = cityAPI;
        this.countyAPI = countyAPI;

        provinceAdapter = new AreaAdapter(provinceAPI.getList());
        cityAdapter = new AreaAdapter(cityAPI.getList());
        countyAdapter = new AreaAdapter(countyAPI.getList());

        spnProvince.setAdapter(provinceAdapter);
        spnCity.setAdapter(cityAdapter);
        spnCounty.setAdapter(countyAdapter);

        loadProvince();
    }

    /**
     * 初始化view
     * @param context
     */
    private void initView(Context context) {
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.view_province, this, false);
        addView(view);
        spnProvince = (Spinner) view.findViewById(R.id.spn_province);
        spnCity = (Spinner) view.findViewById(R.id.spn_city);
        spnCounty = (Spinner) view.findViewById(R.id.spn_county);



        spnProvince.setEnabled(false);
        spnCity.setEnabled(false);
        spnCounty.setEnabled(false);
    }

    /**
     * 配置联动
     */
    private void setLinkMove(){
        //初始加载完成之后再配置联动
        if(spnProvince.getOnItemSelectedListener() == null){
            spnProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    KV kv = (KV) parent.getItemAtPosition(position);
                    cityAPI.setId(kv.key);
                    loadCity();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if(spnCity.getOnItemSelectedListener() == null){
            spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    KV kv = (KV) parent.getItemAtPosition(position);
                    countyAPI.setId(kv.key);
                    loadCounty();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    /**
     * 加载省
     */
    private void loadProvince() {
        provinceAPI.doConnect(new WebAPIListener() {
            @Override
            public void onStart() {
                spnCity.setEnabled(false);
                spnCounty.setEnabled(false);
                finish = false;
                if(onLoadListener != null){
                    onLoadListener.onLoading();
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(String result) {
                spnProvince.setEnabled(true);
                int position = findSelection(provinceAPI.getList(), province);
                if(position != -1){
                    spnProvince.setSelection(position);
                    cityAPI.setId(provinceAPI.getList().get(position).key);
                    provinceAdapter.notifyDataSetChanged();
                    loadCity();
                }
            }

            @Override
            public void onFail(int errCode, String errMessage) {
                DialogUtils.getInstance(context).showCommitDialog("错误", errMessage);
            }
        });
    }

    /**
     * 加载市
     */
    private void loadCity() {
        cityAPI.doConnect(new WebAPIListener() {
            @Override
            public void onStart() {
                spnCounty.setEnabled(false);
                finish = false;
                if(onLoadListener != null){
                    onLoadListener.onLoading();
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(String result) {
                spnCity.setEnabled(true);
                int position = findSelection(cityAPI.getList(), city);
                if(position != -1){
                    spnCity.setSelection(position);
                    countyAPI.setId(cityAPI.getList().get(position).key);
                    cityAdapter.notifyDataSetChanged();
                    loadCounty();
                }
            }

            @Override
            public void onFail(int errCode, String errMessage) {
                DialogUtils.getInstance(context).showCommitDialog("错误", errMessage);
            }
        });
    }

    /**
     * 加载区
     */
    private void loadCounty() {
        countyAPI.doConnect(new WebAPIListener() {
            @Override
            public void onStart() {
                finish = false;
            }

            @Override
            public void onFinish() {
                finish = true;
                if(onLoadListener != null){
                    onLoadListener.onFinishLoad();
                }
            }

            @Override
            public void onSuccess(String result) {
                spnCounty.setEnabled(true);
                int position = findSelection(countyAPI.getList(), county);
                if(position != -1){
                    countyAdapter.notifyDataSetChanged();
                    spnCounty.setSelection(position);
                }
                //加载区完成后，将初始值归null
                province = null;
                city = null;
                county = null;

                setLinkMove();
            }

            @Override
            public void onFail(int errCode, String errMessage) {
                DialogUtils.getInstance(context).showCommitDialog("错误", errMessage);
            }
        });
    }

    /**
     * 找到对应的选项
     * @param id id
     * @return  返回同id所在的位置，默认返回0， 如果列表为空，返回-1
     */
    private int findSelection(List<KV> list, String id){
        if(id == null){
            return 0;
        }
        if(list.size() == 0){
            return -1;
        }
        int position = 0;
        for(int i = 0 ; i < list.size(); i ++){
            KV kv = list.get(i);
            if(kv.key.equals(id)){
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.spnProvince.setClickable(enabled);
        this.spnProvince.setEnabled(enabled);
        this.spnCity.setClickable(enabled);
        this.spnCity.setEnabled(enabled);
        this.spnCounty.setClickable(enabled);
        this.spnCounty.setEnabled(enabled);
    }
}
