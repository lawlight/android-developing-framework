package hz.com.androidlib.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hz.lib.paging.DividerItemDecoration;
import com.hz.lib.paging.ListPagingFragment;
import com.hz.lib.webapi.WebAPIListener;

import java.util.List;

/**
 * 网格布局的分页列表demo
 * Created by LiuPeng on 16/9/22.
 */
public class GridListDemoFragment extends ListPagingFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //添加水平列表的分割线（竖线），默认只有垂直列表的分割线（横线）。
        getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));
    }

    @Override
    protected void loadData(int pageIndex) {
        //数据与其他是一样的
        final ListDemoAPI api = new ListDemoAPI(getActivity());
        api.pageNumber = pageIndex;
        api.pageSize = 20;
        api.doHttpGet(new WebAPIListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(String result) {
                onLoaded(api.list);
            }

            @Override
            public void onFail(int errCode, String errMessage) {
                onError(errMessage);
            }
        });
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List list) {
        return new GridListDemoAdapter(list);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        //这里可以指定网格布局的列数
        return new GridLayoutManager(getActivity(), 3);
    }
}
