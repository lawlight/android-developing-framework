package hz.com.androidlib.list;

import android.support.v7.widget.RecyclerView;

import com.hz.lib.paging.ListPagingFragment;
import com.hz.lib.webapi.WebAPIListener;

import java.util.List;

/**
 * 线性分页demo fragment
 * Created by LiuPeng on 16/6/12.
 */
public class LinearListDemoFragment extends ListPagingFragment {

    @Override
    protected void loadData(int pageIndex) {
        final ListDemoAPI api = new ListDemoAPI(getActivity());
        api.pageNumber = pageIndex;
        api.pageSize = 10;
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
        return new LinearListDemoAdapter(list);
    }

}
