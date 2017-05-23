package hz.com.androidlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import hz.com.androidlib.R;

/**
 * @Created by lenovo on 2016/9/21.
 */
public class DialogDemoActivity extends AppCompatActivity implements View.OnClickListener {


    private Button confirmbtn, morebtn, samplembtn, siglebtn, multibtn, selfviewbtn, selfdialogbtn;
    private Button popofenbtn, popselfbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        confirmbtn = (Button) findViewById(R.id.confirmbtn);
        confirmbtn.setOnClickListener(this);

        morebtn = (Button) findViewById(R.id.morebtn);
        morebtn.setOnClickListener(this);

        samplembtn = (Button) findViewById(R.id.samplembtn);
        samplembtn.setOnClickListener(this);

        siglebtn = (Button) findViewById(R.id.siglebtn);
        siglebtn.setOnClickListener(this);

        multibtn = (Button) findViewById(R.id.multibtn);
        multibtn.setOnClickListener(this);

        selfviewbtn = (Button) findViewById(R.id.selfviewbtn);
        selfviewbtn.setOnClickListener(this);

        selfdialogbtn = (Button) findViewById(R.id.selfdialogbtn);
        selfdialogbtn.setOnClickListener(this);

        popofenbtn = (Button) findViewById(R.id.popofenbtn);
        popofenbtn.setOnClickListener(this);

        popselfbtn = (Button) findViewById(R.id.popselfbtn);
        popselfbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //继承dialog
            //自定义一个类继承自Dialog类，然后在构造方法中，定义这个dialog的布局和一些初始化信息。
            case R.id.selfdialogbtn:
                final NearBySelectDialog nearBySelectDialog =
                        new NearBySelectDialog(DialogDemoActivity.this);
                nearBySelectDialog.showDilog(new MyOkButtonClict() {
                    @Override
                    public void onClick(String str) {

                        try {
                            JSONObject object = new JSONObject((String) str);
                            if (object != null) {
//                                sexstr=object.optString("sex");
//                                actstr=object.optString("active");
//                                orderstr=object.optString("sort");
//                                agestr=object.optString("age");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nearBySelectDialog.dismiss();
                    }
                });

                break;
            //确认
            case R.id.confirmbtn:

                AlertDialog.Builder builder = new AlertDialog.Builder(DialogDemoActivity.this);
                builder.setMessage("确认退出吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            //多个操作按钮
            case R.id.morebtn:
                final Dialog dialog = new AlertDialog.Builder(this).setIcon(
                        android.R.drawable.btn_star).setTitle("喜好调查").setMessage(
                        "你喜欢李连杰的电影吗？").setPositiveButton("很喜欢",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(DialogDemoActivity.this, "我很喜欢他的电影。",
                                        Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("不喜欢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(DialogDemoActivity.this, "我不喜欢他的电影。",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }).setNeutralButton("一般", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Toast.makeText(DialogDemoActivity.this, "谈不上喜欢不喜欢。",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }).create();
                dialog.show();
                break;
            //多选框
            case R.id.multibtn:

                final boolean[] flags = new boolean[]{false, false};
                final String[] items = new String[]{"Item1", "Item2"};
                new AlertDialog.Builder(this).setTitle("复选框").setMultiChoiceItems(
                        items, flags, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                flags[i] = b;
                                String result = "您选择了：";
                                for (int j = 0; j < flags.length; j++) {
                                    if (flags[i]) {
                                        result = result + items[j] + "、";
                                    }
                                }
                            }
                        })
                        //添加一个确定按钮
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("取消", null).show();
                break;
            // 单选框
            case R.id.siglebtn:
                final String[] items2 = new String[]{"Item1", "Item2"};
                new AlertDialog.Builder(this).setTitle("单选框").setIcon(
                        android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                        items2, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                break;
            //简单view对话框
            case R.id.samplembtn:
                new AlertDialog.Builder(this).setTitle("请输入").setIcon(
                        android.R.drawable.ic_dialog_info).setView(
                        new EditText(this)).setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).show();
                break;
            //自定义view
            case R.id.selfviewbtn:
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog,
                        (ViewGroup) findViewById(R.id.dialog));
                new AlertDialog.Builder(this).setTitle("自定义布局").setView(layout)
                        .setPositiveButton("确定", null)
                        .setNegativeButton("取消", null).show();
                break;


            case R.id.popofenbtn:
                showPopupWindow(popofenbtn);
                break;
            case R.id.popselfbtn:
                break;
        }

    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(DialogDemoActivity.this).inflate(
                R.layout.pop_window, null);
        // 设置按钮的点击事件
        Button button = (Button) contentView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(DialogDemoActivity.this, "button is pressed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                GridLayout.LayoutParams.WRAP_CONTENT,
                GridLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.popwindow_bg));


        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        int xPos = -popupWindow.getWidth() / 2
//                + view.getWidth() / 2;
//
//        popupWindow.showAsDropDown(view, xPos, 4);


        //显示屏幕的 左右边
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //获取xoff
        int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        //xoff,yoff基于anchor的左下角进行偏移。


//        int xpos = (manager.getDefaultDisplay().getWidth() / 10)*3  - view.getWidth() / 2;

        popupWindow.showAsDropDown(view, xpos, 0);
        //监听

        // 设置好参数之后再show  下方
//        popupWindow.showAsDropDown(view);

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //上方
//        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

        //左方
//        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth(), location[1]);


        //右方
//        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + v.getWidth(), location[1]);

    }


}
