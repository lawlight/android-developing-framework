package hz.com.androidlib.list;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.hz.lib.paging.ListPagingFragment;
import com.hz.lib.webapi.WebAPIListener;

import java.util.List;

/**
 * 网格布局的分页列表demo
 * Created by LiuPeng on 16/9/22.
 */
public class StaggeredListDemoFragment extends ListPagingFragment {

    @Override
    protected void loadData(int pageIndex) {
        //数据与其他是一样的
        final ListDemoAPI api = new ListDemoAPI(getActivity());
        api.pageNumber = pageIndex;
        api.pageSize = 6;
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
        return new StaggeredListDemoAdapter(list);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        //这里可以指定网格布局的列数
        return new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }
}
