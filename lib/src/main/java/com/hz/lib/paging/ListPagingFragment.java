package com.hz.lib.paging;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hz.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 可分页List的Fragment，使用时继承此类，重写ListPageable接口中的方法来绑定Adapter、重写加载数据方法。
 * 在子类中加载数据完成后，必须调用本类中的onLoaded方法来通知刷新显示。
 * 可选择性重写个体LayoutManager等方法来定制RecyclerView外观
 * 可重写getIndexStart来定义页码的开始索引
 */
public abstract class ListPagingFragment extends Fragment {

    private View view;
    private SwipeRefreshLayout swipe;

    //存放所有数据的列表
    private List list = new ArrayList<>();

    //列表RecyclerView相关
    private RecyclerView recyclerView;

    //加载更多的listener
    OnLoadMoreListener mOnLoadMoreListener;

    //当前是否正在加载
    private boolean isLoading = false;
    //是否有更多数据
    private boolean isHasMore = true;
    //记录上次滑动到的位置
    private int lastPosition = 0;

    //是否启动刷新和加载
    private boolean mRefreshEnable = true;
    private boolean mLoadMoreEnable = true;

    //分页
    private int pageIndex = 1;

    /**
     * 可重写此方法，获取页面开始索引，默认为1
     * @return
     */
    public int getIndexStart(){
        return 1;
    }

    /**
     * 重写此方法，可修改默认的布局文件，但是布局文件中必须include R.layout.fragment_page_list
     * @return
     */
    protected int getLayoutId(){
        return R.layout.fragment_page_list;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(getLayoutId(),container,false);
            initView();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化页码
        this.pageIndex = getIndexStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(list.size() == 0 && this.pageIndex == getIndexStart()){
            list.clear();
            load();
        }else{
            //移动到上次移动的位置
            recyclerView.getLayoutManager().scrollToPosition(lastPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存这次滑动到的位置，在onResume时还原
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if(lm instanceof LinearLayoutManager){
            lastPosition = ((LinearLayoutManager)lm).findFirstVisibleItemPosition();
        }else if(lm instanceof StaggeredGridLayoutManager){
            lastPosition = ((StaggeredGridLayoutManager)lm).findFirstVisibleItemPositions(null)[0];
        }else{
            lastPosition = 0;
        }

        recyclerView.getLayoutManager().computeVerticalScrollOffset(new RecyclerView.State());
    }

    /**
     * 加载数据，加载完成后需要回调本类中的onLoaded方法，传入是否加载成功的入参，错误时调用onerror方法
     * @param pageIndex 页码
     */
    protected abstract void loadData(int pageIndex);

    /**
     * 获取Adapter
     * @param list
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter(List list);

    //RecyclerView的属性

    /**
     * 是否是固定高度的List，默认true
     * @return
     */
    public boolean getHasFixedSize(){
        return true;
    }

    /**
     * 获取LayoutManager，默认LinearLayoutManager
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager(){
        return new LinearLayoutManager(getActivity());
    }

    /**
     * 获取ItemDecoration，默认水平分割线的ItemDecoration
     * 如果要更改分割线，重写此方法即可，如果添加分割线，在fragment初始化后调用getRecyclerView方法，执行addItemDecoration
     * @return
     */
    public RecyclerView.ItemDecoration getItemDecoration(){
        return new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
    }

    private void initView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.fpl_rv_list);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.fpl_srl_swipe);

        //初始化加载更多监听
        mOnLoadMoreListener = new OnLoadMoreListener() {
            @Override
            protected void onLoadMore() {
                load();
            }
        };

        //开启下拉刷新，下拉加载更多
        setRefreshEnable();

        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(getAdapter(list));
        recyclerView.setHasFixedSize(getHasFixedSize());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if(itemDecoration != null){
            recyclerView.addItemDecoration(getItemDecoration());
        }
    }

    /**
     * 刷新数据
     */
    public final void refreshData() {
        pageIndex = getIndexStart();
        isHasMore = true;
        load();
    }

    /**
     * 清除数据
     */
    public void clearData(){
        pageIndex = getIndexStart();
        isHasMore = true;
        list.clear();
        if(recyclerView != null || recyclerView.getAdapter() != null){
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 加载数据
     */
    private void load(){
        Log.d("debug", "开始加载");
        if(isLoading == false && isHasMore) {
            isLoading = true;
            setRefresh(true);
            loadData(pageIndex);
        }
    }

    /**
     * 加载完成，在子类加载数据完成后，需回掉此接口
     * @param pageList 这一页的数据
     */
    protected final void onLoaded(List pageList){
        Log.d("debug", "加载完毕");
        setRefresh(false);
        if(pageList == null || pageList.size() == 0){
            //没有获取到数据
            isHasMore = false;
            if(pageIndex == getIndexStart()){
                //第一页时表示一条数据也没有
                list.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                onEmpty();
            }else{
                //没有加载到更多数据
                onLoadNoMore();
            }
        }else{
            //获取到了数据
            //第一页时清空之前数据
            if(pageIndex == getIndexStart()){
                list.clear();
            }
            list.addAll(pageList);
            recyclerView.getAdapter().notifyDataSetChanged();
            pageIndex++;
            isHasMore = true;
        }
        isLoading = false;
    }

    /**
     * 错误时调用此方法
     * @param errMessage
     */
    protected void onError(String errMessage){
        Log.d("debug", "加载失败");
        isLoading = false;
    }

    /**
     * 数据为空时，可以在子类重写此方法，自定义数据为空时的逻辑
     */
    protected void onEmpty(){
        Log.d("debug", "一条数据也没有");
        Toast.makeText(getActivity(), "无数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 没有更多数据时的加载，可在子类重写此方法，自定义无更多数据时的逻辑
     */
    protected void onLoadNoMore(){
        Log.d("debug", "没有更多数据了");
        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置SwipeRefreshLayout刷新
     * @param isRefreshing
     */
    private void setRefresh(final boolean isRefreshing){

        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * 获取数据为空时的View，可重写此方法换成自定义的View
     * @return
     */
    public View getEmptyView(){
        return null;
    }

    /**
     * 设置刷新启用
     */
    public void setRefreshEnable(boolean refreshEnable,boolean loadMoreEnable){
        this.mRefreshEnable = refreshEnable;
        this.mLoadMoreEnable = loadMoreEnable;
    }

    /**
     * 私有设置是否启动刷新
     */
    private void setRefreshEnable(){
        if(mRefreshEnable){
            //下拉刷新
            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshData();
                }
            });
            swipe.setEnabled(true);
        }else{
            swipe.setEnabled(false);
        }
        if(mLoadMoreEnable){
            //上滑加载更多
            recyclerView.addOnScrollListener(mOnLoadMoreListener);
        }else{
            recyclerView.removeOnScrollListener(mOnLoadMoreListener);
        }
    }

    /**
     * 获取RecyclerView，注意，因为recyclerView是在onCreateView生命周期后初始化的，
     * 所以在子类的onStart之后才能获取到Fragment中的RecyclerView
     * @return
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 用于RecyclerView加载更多的监听器
     */
    public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
        private int lastVisibleItem;
        /**
         * 重写此抽象方法，实现加载更多
         */
        protected abstract void onLoadMore();

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 >= recyclerView.getAdapter().getItemCount()) {
                onLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //获取滑动的最后一项的位置
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if(lm instanceof LinearLayoutManager){
                lastVisibleItem = ((LinearLayoutManager) lm)
                        .findLastVisibleItemPosition();
            }else if(lm instanceof StaggeredGridLayoutManager){
                //以第0列为基础
                lastVisibleItem = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(null)[0] * ((StaggeredGridLayoutManager) lm).getSpanCount();

            }

        }
    }
}
