package com.physis.kit.module.mqtt;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.physicomtech.kit.physislibrary.PHYSIsMQTTActivity;
import com.physis.kit.module.R;
import com.physis.kit.module.dialog.LoadingDialog;

public class LedButtonMqttActivity extends PHYSIsMQTTActivity implements View.OnClickListener {

    private Button btnConnect, btnOn, btnOff;
    private EditText etSerialNum, etPubTopic, etSubTopic;
    private TextView tvBtnState;

    private String serialNum = null;
    private String pubTopic = null;
    private String subTopic = null;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_button_mqtt);

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
        if(!this.serialNum.equals(serialNum) || !subTopic.equals(topic))
            return;

        showButtonState(data);
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
            }
        }
    }

    private void showButtonState(String msg){
        try{
            int state = Integer.parseInt(msg);
            tvBtnState.setText(state == 0 ? "Unclicked" : "Clicked");
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setConnectedResult(boolean state){
        LoadingDialog.dismiss();

        if(isConnected = state) {
            btnConnect.setText("MQTT 연결 종료");
            startSubscribe(serialNum, subTopic);
        }

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
        subTopic = etSubTopic.getText().toString();

        if(serialNum.length() != 12){
            Toast.makeText(getApplicationContext(), "PHYSIs Kit의 시리얼 넘버를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pubTopic.length() == 0 || subTopic.length() == 0){
            Toast.makeText(getApplicationContext(), "Topic 정보를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isConnected){
            disconnectMQTT();
        }else{
            LoadingDialog.show(LedButtonMqttActivity.this, "Connecting..");
            connectMQTT();
        }
    }

    private void init() {
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);
        btnOn = findViewById(R.id.btn_on);
        btnOn.setOnClickListener(this);
        btnOff = findViewById(R.id.btn_off);
        btnOff.setOnClickListener(this);

        etSerialNum = findViewById(R.id.et_serial_num);
        etPubTopic = findViewById(R.id.et_pub_topic);
        etSubTopic = findViewById(R.id.et_sub_topic);

        tvBtnState = findViewById(R.id.tv_btn_state);

        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();
    }

}
