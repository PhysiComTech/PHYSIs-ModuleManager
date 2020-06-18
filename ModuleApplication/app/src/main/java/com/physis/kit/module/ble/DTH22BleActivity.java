package com.physis.kit.module.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.physicomtech.kit.physislibrary.PHYSIsBLEActivity;
import com.physis.kit.module.R;
import com.physis.kit.module.dialog.LoadingDialog;

public class DTH22BleActivity extends PHYSIsBLEActivity implements View.OnClickListener {

    private static final String TAG = DTH22BleActivity.class.getSimpleName();

    private Button btnConnect;
    private EditText etSerialNum;
    private TextView tvHumidity, tvTemperature;

    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_dth22);

        init();
    }


    @Override
    protected void onBLEConnectedStatus(int result) {
        super.onBLEConnectedStatus(result);
        setConnectedResult(result);
    }

    @Override
    protected void onBLEReceiveMsg(String msg) {
        super.onBLEReceiveMsg(msg);
        showDTHValues(msg);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_connect)
            connect();
    }


    @SuppressLint("SetTextI18n")
    private void showDTHValues(String msg){
        String[] datas = msg.split(",");

        if(datas.length != 2)
            return;

        tvHumidity.setText(datas[0] + " %");
        tvTemperature.setText(datas[1] + " ℃");
    }

    private void connect(){
        String serialNum = etSerialNum.getText().toString();
        if(serialNum.length() != 12){
            Toast.makeText(getApplicationContext(), "PHYSIs Kit의 시리얼 넘버를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isConnected){
            disconnectDevice();
        }else{
            LoadingDialog.show(DTH22BleActivity.this, "Connecting..");
            connectDevice(serialNum);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setConnectedResult(int state){
        // set button
        if(isConnected = state == CONNECTED){
            btnConnect.setText("디바이스 연결 종료");
        }else{
            btnConnect.setText("디바이스 연결");
        }
        // show toast
        String toastMsg;
        if(state == CONNECTED){
            toastMsg = "Physi Kit와 연결되었습니다.";
        }else if(state == DISCONNECTED){
            toastMsg = "Physi Kit 연결이 실패/종료되었습니다.";
        }else{
            toastMsg = "연결할 Physi Kit가 존재하지 않습니다.";
        }
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
        LoadingDialog.dismiss();
    }


    private void init() {
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);

        etSerialNum = findViewById(R.id.et_serial_num);

        tvHumidity = findViewById(R.id.tv_humidity);
        tvTemperature = findViewById(R.id.tv_temperature);

        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();
    }


}
