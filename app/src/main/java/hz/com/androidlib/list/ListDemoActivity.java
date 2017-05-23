package hz.com.androidlib.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hz.com.androidlib.R;

public class ListDemoActivity extends AppCompatActivity {

    LinearListDemoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demo);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new LinearListDemoFragment()).commit();
    }

    @OnClick({R.id.btn_linear, R.id.btn_grid, R.id.btn_staggered})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_linear:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new LinearListDemoFragment()).commit();
                break;
            case R.id.btn_grid:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new GridListDemoFragment()).commit();
                break;
            case R.id.btn_staggered:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new StaggeredListDemoFragment()).commit();
                break;
        }
    }
}
