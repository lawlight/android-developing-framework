package hz.com.androidlib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hz.lib.utils.CameraUtils;

import org.xutils.x;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.view.ImageSelectorView;

/**
 * 图片选择Activity
 */
public class ImageActivity extends AppCompatActivity {

    @Bind(R.id.imageSelector)
    ImageSelectorView imageSelector;
    @Bind(R.id.sw_multi)
    Switch swMulti;
    @Bind(R.id.sw_camera)
    Switch swCamera;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_console)
    TextView tvConsole;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.iv_result)
    ImageView ivResult;

    CameraUtils cameraUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        cameraUtils = new CameraUtils(this, this);

        imageSelector.register(this, 2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    progress = 1;
                }
                tvNum.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick({R.id.btn_select, R.id.btn_camera, R.id.btn_album})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                //打开图片选择器
                Intent imageIntent = new Intent(this, MultiImageSelectorActivity.class);
                imageIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, swMulti.isChecked() ?
                        MultiImageSelectorActivity.MODE_MULTI : MultiImageSelectorActivity.MODE_SINGLE);
                int progress = seekBar.getProgress();
                if(progress < 1){
                    progress = 1;
                }
                imageIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, progress);

                imageIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, swCamera.isChecked());

                startActivityForResult(imageIntent, 1);
                break;
            case R.id.btn_camera:
                //拍照
                cameraUtils.startCameraOrAlbumActivity(1, null, 1000);
                break;
            case R.id.btn_album:
                cameraUtils.startCameraOrAlbumActivity(2, null, 2000);
                //相册
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ivResult.setVisibility(View.GONE);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                tvConsole.setText("图片选择器返回结果：\n");
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for(int i = 0 ; i < path.size() ; i++){
                    tvConsole.append(path.get(i)+"\n");
                }
            }else if(requestCode == 1000 || requestCode == 2000){
                ivResult.setVisibility(View.VISIBLE);
                String image = cameraUtils.onActivityResult(requestCode, resultCode, data);
                x.image().bind(ivResult, image);
                tvConsole.setText((requestCode == 1000 ? "相机" : "相册") + "相册返回结果：\n" + image);
            }
        }
        imageSelector.onActivityResult(requestCode, resultCode, data);
    }
}
