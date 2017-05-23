package me.nereo.multi_image_selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.view.ImageViewPager;

/**
 * 显示图片预览的Activity，ImageSelectorView选择进入的大图预览
 */
public class ImageViewPagerActivity extends AppCompatActivity {

    //要显示的图片
    public static String EXTRA_IMAGES = "images";
    //默认显示第几张
    public static String EXTRA_INDEX = "index";
    //是否显示删除按钮
    public static String EXTRA_SHOW_DELETE = "showDelete";

    private ImageViewPager viewPager;
    private Button btnDel;
    private TextView tvIndex;

    private int currentItemIndex = 0;
    private List<String> images;
    private boolean showDeleteButton = false;

    private boolean deleteFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_pager);
        images = getIntent().getStringArrayListExtra(EXTRA_IMAGES);
        currentItemIndex = getIntent().getIntExtra(EXTRA_INDEX, 0);
        showDeleteButton = getIntent().getBooleanExtra(EXTRA_SHOW_DELETE, false);

        tvIndex = (TextView) findViewById(R.id.aivp_tv_index);
        tvIndex.setText((currentItemIndex + 1) + "/" + images.size());

        viewPager = (ImageViewPager) findViewById(R.id.aivp_viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItemIndex = position;
                tvIndex.setText((position + 1) + "/" + images.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setImages(images);
        viewPager.setCurrentItem(currentItemIndex);

        btnDel = (Button) findViewById(R.id.aivp_btn_del);
        if(showDeleteButton){
            btnDel.setVisibility(View.VISIBLE);
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.remove(currentItemIndex);
                    deleteFlag = true;
                    if (images.size() == 0) {
                        back(null);
                        return;
                    }
                    if (currentItemIndex == images.size()) {
                        currentItemIndex--;
                    }
                    tvIndex.setText((currentItemIndex + 1) + "/" + images.size());
                }
            });
        }else{
            btnDel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        back(null);
    }

    /**
     * 返回
     * @param view
     */
    public void back(View view){
        if(deleteFlag){
            Intent intent = new Intent();
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT,
                    (ArrayList<String>) images);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

}
