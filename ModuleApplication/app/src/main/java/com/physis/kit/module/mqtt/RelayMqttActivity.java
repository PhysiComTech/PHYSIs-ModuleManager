package com.physis.kit.module.mqtt;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.physicomtech.kit.physislibrary.PHYSIsMQTTActivity;
import com.physis.kit.module.R;
import com.physis.kit.module.dialog.LoadingDialog;

public class RelayMqttActivity extends PHYSIsMQTTActivity implements View.OnClickListener {

    private Button btnConnect, btnSetup, btnOn, btnOff;
    private EditText etSerialNum, etPubTopic, etPWMValue;

    private String serialNum = null;
    private String pubTopic = null;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relay_mqtt);

        init();
    }

    @Override
    protected void onMQTTConnectedStatus(boolean result) {
        super.onMQTTConnectedStatus(result);
        setConnectedResult(result);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onMQTTDisconnected() {
        super.onMQTTDisconnected();
        isConnected = false;
        btnConnect.setText("MQTT 연결");
    }

    @Override
    protected void onSubscribeListener(String serialNum, String topic, String data) {
        super.onSubscribeListener(serialNum, topic, data);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_connect){
            connect();
        }else{
            if(!isConnected)
                Toast.makeText(getApplicationContext(),
                        "MQTT를 연결 연결하세요.",
                        Toast.LENGTH_SHORT).show();

            switch (v.getId()){
                case R.id.btn_on:
                    publish(serialNum, pubTopic,"ON");
                    break;
                case R.id.btn_off:
                    publish(serialNum, pubTopic,"OFF");
                    break;
                case R.id.btn_setup:
                    String volume = etPWMValue.getText().toString();
                    if(volume.length() == 0) return;
                    publish(serialNum, pubTopic, volume);
                    break;
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void setConnectedResult(boolean state){
        LoadingDialog.dismiss();

        if(isConnected = state)
            btnConnect.setText("MQTT 연결 종료");

        String toastMsg;
        if(isConnected) {
            toastMsg = "PHYSIs MQTT Broker 연결에 성공하였습니다.";
        } else {
            toastMsg = "PHYSIs MQTT Broker 연결에 실패하였습니다.";
        }
        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    private void connect(){
        serialNum = etSerialNum.getText().toString();
        pubTopic = etPubTopic.getText().toString();

        if(serialNum.length() != 12){
            Toast.makeText(getApplicationContext(), "PHYSIs Kit의 시리얼 넘버를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pubTopic.length() == 0){
            Toast.makeText(getApplicationContext(), "Publish Topic을 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isConnected){
            disconnectMQTT();
        }else{
            LoadingDialog.show(RelayMqttActivity.this, "Connecting..");
            connectMQTT();
        }
    }

    private void init() {
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);
        btnSetup = findViewById(R.id.btn_setup);
        btnSetup.setOnClickListener(this);
        btnOn = findViewById(R.id.btn_on);
        btnOn.setOnClickListener(this);
        btnOff = findViewById(R.id.btn_off);
        btnOff.setOnClickListener(this);

        etSerialNum = findViewById(R.id.et_serial_num);
        etPubTopic = findViewById(R.id.et_pub_topic);
        etPWMValue = findViewById(R.id.et_pwm_value);

        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();
    }

}
