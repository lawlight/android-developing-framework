package hz.com.androidlib.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import hz.com.androidlib.R;

public class ListTargetActivity extends AppCompatActivity {

    @Bind(R.id.textView8)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_target);
        ButterKnife.bind(this);

        textView.setText(getIntent().getStringExtra("text"));
    }
}
