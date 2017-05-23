package com.hz.qrscan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hz.qrscan.R;

public class QRMainActivity extends AppCompatActivity{

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrmain);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void onScan(View view){
        startActivityForResult(new Intent(this, ScanActivity.class), 1);
    }

    public void onDecode(View view){
        startActivity(new Intent(this, DecodeActivity.class));
    }

    public void onGenerate(View view){
        startActivity(new Intent(this, GenerateActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                tvResult.setText(data.getStringExtra(ScanActivity.EXTRA_RESULT));
            }
        }
    }
}
