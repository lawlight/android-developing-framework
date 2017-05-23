package hz.com.androidlib.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hz.com.androidlib.R;

public class TabIndexDemoActivity extends AppCompatActivity {

    @Bind(R.id.iv_tab1)
    ImageView ivTab1;
    @Bind(R.id.tv_tab1)
    TextView tvTab1;
    @Bind(R.id.iv_tab2)
    ImageView ivTab2;
    @Bind(R.id.tv_tab2)
    TextView tvTab2;
    @Bind(R.id.iv_tab3)
    ImageView ivTab3;
    @Bind(R.id.tv_tab3)
    TextView tvTab3;
    @Bind(R.id.iv_tab4)
    ImageView ivTab4;
    @Bind(R.id.tv_tab4)
    TextView tvTab4;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;

    Fragment[] fragments = new Fragment[4];
    ImageView[] images;
    TextView[] texts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_index_demo);
        ButterKnife.bind(this);
        images = new ImageView[]{ivTab1, ivTab2, ivTab3, ivTab4};
        texts = new TextView[]{tvTab1, tvTab2, tvTab3, tvTab4};

        selectFragment(0);
    }

    @OnClick({R.id.ll_tab1, R.id.ll_tab2, R.id.ll_tab3, R.id.ll_tab4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_tab1:
                selectFragment(0);
                break;
            case R.id.ll_tab2:
                selectFragment(1);
                break;
            case R.id.ll_tab3:
                selectFragment(2);
                break;
            case R.id.ll_tab4:
                selectFragment(3);
                break;
        }
    }

    private void selectFragment(int index){
        //清除选择
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if(fragments[index] == null){
            fragments[index] = getFragment(index);
        }
        if(fragments != null){
            if(!fragments[index].isAdded()){
                transaction.add(R.id.container, fragments[index]);
            }
            transaction.show(fragments[index]);
        }
        //更新按钮状态
        texts[index].setSelected(true);
        images[index].setSelected(true);
        transaction.commit();
    }

    /**
     * 隐藏所有的fragment
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction){
        for(int i = 0 ; i < fragments.length; i ++){
            if(fragments[i] != null){
                transaction.hide(fragments[i]);
                texts[i].setSelected(false);
                images[i].setSelected(false);
            }
        }
    }

    /**
     * 获取对应的fragment
     * @param index
     * @return
     */
    private Fragment getFragment(int index){
        switch (index){
            case 0:
                return IndexDemoFragment.newInstance("微博页");
            case 1:
                return IndexDemoFragment.newInstance("微信页");
            case 2:
                return IndexDemoFragment.newInstance("QQ页");
            case 3:
                return IndexDemoFragment.newInstance("空间页");
            default:
                return null;
        }
    }
}
