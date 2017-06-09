package hz.com.androidlib.record;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.buihha.audiorecorder.Mp3Recorder;
import com.buihha.audiorecorder.Voice;
import com.hz.lib.utils.DialogUtils;

import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hz.com.androidlib.R;

/**
 * 录音demo界面
 * @author LP
 *
 */
public class RecordDemoActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    //UI
    private ListView lvVoice;
    private Button btnVoice;


    private Button btnRestart;
    private Button btnRecordDone;
    private TextView tvCurTime;
    private TextView tvTotalTime;

    //录音
    Mp3Recorder mp3Recorder = new Mp3Recorder();
    List<Voice> voices = new ArrayList<Voice>();
    VoiceAdapter adapter;

    MediaPlayer mediaPlayer;
    MediaPlayer curMediaPlayer;

    //录音计时
    long startTime;
    long voiceTime;
    String voiceTimeString = "00:00";	//mm:SS格式的时间

    //录音状态
    private enum RecordType{
        WAIT,	//等待开始录音
        RECORDING,	//正在录音
        STOP,	//停止录音
        PLAYING,	//正在播放
        STOPPLAYING //停止播放
    }

    boolean recordDone = false;

    private Animation recordAnimation;
    private ImageView ivAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_demo);

        initView();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                adapter.cancelAnim();
            }
        });
        curMediaPlayer = new MediaPlayer();
        curMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                ChangeRecordUIState(RecordType.STOPPLAYING);
            }
        });
    }

    private void initView(){

        lvVoice = (ListView)findViewById(R.id.consult_lv_voice);
        adapter = new VoiceAdapter();

        lvVoice.setAdapter(adapter);
        btnVoice = (Button)findViewById(R.id.consult_btn_voice);
        //重新录音
        btnRestart = (Button)findViewById(R.id.consult_btn_rerecord);
        btnRestart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if(mp3Recorder.isRecording()){
                        mp3Recorder.stopRecording();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recordDone = false;
                ChangeRecordUIState(RecordType.WAIT);
            }
        });
        btnRecordDone = (Button)findViewById(R.id.consult_btn_recorddone);
        btnRecordDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addRcordToList();
                recordDone = false;
            }
        });
        tvCurTime = (TextView)findViewById(R.id.consult_tv_curtime);
        tvTotalTime = (TextView)findViewById(R.id.consult_tv_totaltime);

        //点击录音
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 按下录音按钮，判断是否开始录音、播放录音、暂停播放
                if(recordDone){	//播放录音或暂停录音
                    //暂停播放录音
                    if(curMediaPlayer.isPlaying()){
                        curMediaPlayer.pause();
                        ChangeRecordUIState(RecordType.STOPPLAYING);
                        return;
                    }
                    //播放录音
                    try {
                        curMediaPlayer.start();
                        //开启一个录音计时线程
                        new Thread(){
                            public void run() {
                                while(curMediaPlayer.isPlaying()){
                                    voiceTimeHandler.sendEmptyMessage(2001);
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ChangeRecordUIState(RecordType.PLAYING);
                    return;
                }
                //如果当前正在录音则停止录音
                if(mp3Recorder.isRecording()){	//正在录音，暂停或恢复录音
                    try {
                        mp3Recorder.stopRecording();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                //不在录音，开始录音
                if(voices.size() >= 3){
                    DialogUtils.getInstance(RecordDemoActivity.this).showShortToast("最多传三段音频");
                    return;
                }
                startRecord();
            }
        });

        recordAnimation = AnimationUtils.loadAnimation(this, R.anim.psy_voice_anim);
        ivAnimation = (ImageView)findViewById(R.id.consult_iv_anim);
        recordAnimation.setRepeatCount(Integer.MAX_VALUE);
        recordAnimation.setRepeatMode(Animation.RESTART);
        recordAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                ivAnimation.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivAnimation.setVisibility(View.GONE);
            }
        });
        ChangeRecordUIState(RecordType.WAIT);
    }

    /**
     * 开始录音
     * @throws IOException
     */
    private void startRecord(){

        //权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1001);
            return;
        }

        recordDone = false;
        //录音存储位置,随机生成一个名字
        //mp3Recorder.setMp3FileName(new Date().getTime()+".mp3");
        mp3Recorder.setMp3Directory(x.app().getExternalCacheDir()+"/record");
        mp3Recorder.setMp3FileName("record_"+new Date().getTime()+".mp3");
        try {
            mp3Recorder.startRecording();
            //开启一个计时线程，单段录音不能超过5分钟。
            new Thread(){
                public void run() {
                    try {
                        startTime = new Date().getTime();
                        voiceTime = 0;
                        voiceTimeString = "00:00";
                        //等待录音开启
                        voiceTimeHandler.sendEmptyMessage(1001);
                        Thread.sleep(500);
                        while(mp3Recorder.isRecording()){
                            //计算出这段录音的时间
                            long endTime = new Date().getTime();
                            voiceTime = (endTime - startTime) / 1000;
                            int minute = (int)voiceTime/60;
                            int second = (int)voiceTime%60;
                            voiceTimeString = String.format("%02d", minute)+":"+String.format("%02d", second);
                            voiceTimeHandler.sendEmptyMessage(1001);
                            if(voiceTime >= 300){	//不得超过5分钟
                                voiceTimeHandler.sendEmptyMessage(5);
                                return;
                            }
                            Thread.sleep(500);
                            while(mp3Recorder.isPause()){
                                Thread.sleep(200);
                            }
                        }
                        if(voiceTime < 1){	//录音时间太短
                            voiceTimeHandler.sendEmptyMessage(1);
                            return;
                        }
                        voiceTimeHandler.sendEmptyMessage(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            }.start();
        } catch (IOException e1) {
            DialogUtils.getInstance(RecordDemoActivity.this).showShortToast("录音出现异常:"+e1.getMessage());
            e1.printStackTrace();
        }

    }

    Handler voiceTimeHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            try {
                if(msg.what == 100){	//录音完成
                    mp3Recorder.stopRecording();
                    recordDone = true;
                    //准备好播放
                    curMediaPlayer.reset();
                    curMediaPlayer.setDataSource(mp3Recorder.getMp3FilePath());
                    curMediaPlayer.prepare();
                    ChangeRecordUIState(RecordType.STOP);
                } else if(msg.what == 1001){	//录音时间更新
                    ChangeRecordUIState(RecordType.RECORDING);
                } else if(msg.what == 2001){
                    ChangeRecordUIState(RecordType.PLAYING);
                } else if(msg.what == 5){
                    DialogUtils.getInstance(RecordDemoActivity.this).showShortToast("单段录音不能超过5分钟");
                    mp3Recorder.stopRecording();
                    ChangeRecordUIState(RecordType.WAIT);
                } else if(msg.what == 1){
                    DialogUtils.getInstance(RecordDemoActivity.this).showShortToast("录音时间太短");
                    mp3Recorder.stopRecording();
                    ChangeRecordUIState(RecordType.WAIT);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        };
    };

    /**
     * 将录音文件添加到list
     * @throws IOException
     */
    private void addRcordToList(){
        //将录制完的文件添加到list中
        Voice voice = new Voice();
        int time = (int)voiceTime;
        if(time < 60){
            voice.setTimeString(time+"\"");
        }else{
            voice.setTimeString(time/60 + "'" + time%60 + "\"");
        }
        voice.setUrl(mp3Recorder.getMp3FilePath());
        Log.d("debug","录音文件："+mp3Recorder.getMp3FilePath()+",时长："+voiceTime);

        voices.add(voice);
        adapter.notifyDataSetChanged();
        ChangeRecordUIState(RecordType.WAIT);
    }

    /**
     * 录音状态UI变化
     * @param type 录音状态
     */
    private void ChangeRecordUIState(RecordType type){
        switch (type) {
            case WAIT:	//等待录音
                btnRecordDone.setVisibility(View.GONE);
                btnRestart.setVisibility(View.GONE);
                tvCurTime.setText("");
                tvTotalTime.setText("");
                btnVoice.setBackgroundResource(R.drawable.psy_voice);
                recordAnimation.cancel();
                break;
            case RECORDING:	//正在录音
                btnRecordDone.setVisibility(View.GONE);
                btnRestart.setVisibility(View.VISIBLE);
                tvCurTime.setText(voiceTimeString);
                tvTotalTime.setText("/05:00");
                btnVoice.setBackgroundResource(R.drawable.psy_pause);
                recordAnimation.reset();
                ivAnimation.startAnimation(recordAnimation);
                break;
            case STOP:	//停止录音（录音预览）
                btnRecordDone.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                tvTotalTime.setText(tvCurTime.getText().toString());
                tvCurTime.setText("");
                btnVoice.setBackgroundResource(R.drawable.psy_play);
                recordAnimation.cancel();
                break;
            case PLAYING:	//播放录音
                btnRecordDone.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                //计算当前播放的时间
                Log.d("debug", "当前播放的时间"+curMediaPlayer.getCurrentPosition());
                int time = (int)(curMediaPlayer.getCurrentPosition()/1000);
                tvCurTime.setText(String.format("%02d", time/60)+":"+String.format("%02d", time%60));
                tvTotalTime.setText("/" + voiceTimeString);
                btnVoice.setBackgroundResource(R.drawable.psy_pause);
                recordAnimation.reset();
                ivAnimation.startAnimation(recordAnimation);
                break;
            case STOPPLAYING:	//停止播放（暂停？）
                btnRecordDone.setVisibility(View.VISIBLE);
                btnRestart.setVisibility(View.VISIBLE);
                //播放时间就是暂停时的时间
                tvTotalTime.setText("/" + voiceTimeString);
                btnVoice.setBackgroundResource(R.drawable.psy_play);
                recordAnimation.cancel();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mp3Recorder.stopRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //DataCleanManager.cleanCustomCache(Common.getSDPath()+"/qingdaopaper/voice");
    }

    /**
     * 声音列表
     * @author LP
     *
     */
    class VoiceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return voices.size();
        }

        @Override
        public Object getItem(int position) {
            return voices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void cancelAnim(){
            for(int i = 0;i<getCount();i++){
                Voice voice = (Voice)getItem(i);
                voice.getAd().stop();
                voice.getAd().selectDrawable(0);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.psy_voice_item, parent, false);
            Button btnVoice = (Button)convertView.findViewById(R.id.pvi_btn_voice);
            Button btnDelete = (Button)convertView.findViewById(R.id.pvi_btn_delete);
            ImageView ivAnim = (ImageView)convertView.findViewById(R.id.pvi_iv_anim);
            voices.get(position).setAd((AnimationDrawable)ivAnim.getDrawable());
            final Voice voice = voices.get(position);
            btnVoice.setText(voice.getTimeString());
            btnVoice.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cancelAnim();
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        return;
                    }
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(voice.getUrl());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        voice.getAd().start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btnDelete.setTag(position);
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    voices.remove(voice);
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startRecord();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    DialogUtils.getInstance(this).showShortToast("无法获取录音权限");
                }
                return;
            }
        }
    }

}

