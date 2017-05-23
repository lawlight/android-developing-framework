package hz.com.androidlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import hz.com.androidlib.R;


/**
 * Created by Administrator on 2016/9/7.
 */
public class NearBySelectDialog extends Dialog {
    private Context context;
    private String sex = "0";
    private String sort = "0";
    private String active = "0";
    private String age = "0";


    private RadioGroup sexgroup, sortgroup, activegroup, agegroup;
    private Button btn_ok, btn_cancle;

    public NearBySelectDialog(Context context) {
        super(context, R.style.PauseDialog);
        this.context = context;
    }

    public NearBySelectDialog(Context context, int themeResId) {
        super(context, R.style.PauseDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        // lp.alpha = 0.80f;
        lp.alpha = 1.0f;
        this.getWindow().setAttributes(lp);
    }

    public void showDilog(final MyOkButtonClict listener) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_near_select_view, null);
        sexgroup = (RadioGroup) view.findViewById(R.id.sexgroup);
        sortgroup = (RadioGroup) view.findViewById(R.id.sortgroup);
        activegroup = (RadioGroup) view.findViewById(R.id.activegroup);
        agegroup = (RadioGroup) view.findViewById(R.id.agegroup);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_cancle = (Button) view.findViewById(R.id.btn_cancle);


        //性别
        sexgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.sex_all:
                        sex = "0";
                        break;
                    case R.id.sex_man:
                        sex = "1";
                        break;
                    case R.id.sex_woman:
                        sex = "2";
                        break;
                }
            }
        });
        //排序
        sortgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.sort_1:
                        sort = "1";
                        break;
                    case R.id.sort_2:
                        sort = "2";
                        break;
                }
            }
        });
        //活跃
        activegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.active_0:
                        active = "0";
                        break;
                    case R.id.active_1:
                        active = "1";
                        break;
                    case R.id.active_2:
                        active = "2";
                        break;
                    case R.id.active_3:
                        active = "3";
                        break;
                    case R.id.active_4:
                        active = "4";
                        break;
                }
            }
        });
        //年龄
        agegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.age_1:
                        age = "0";
                        break;
                    case R.id.age_2:
                        age = "1";
                        break;
                    case R.id.age_3:
                        age = "2";
                        break;
                    case R.id.age_4:
                        age = "3";
                        break;
                    case R.id.age_5:
                        age = "4";
                        break;
                }
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    String str = "{\"sex\":\"" + sex + " \",\"active\":\"" + active + " \",\"sort\":\"" + sort + " \",\"age\":\"" + age + " \"}";
                    listener.onClick(str);
                }
                dismiss();

            }
        });


        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(mLayoutParams);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        show();
    }

}
