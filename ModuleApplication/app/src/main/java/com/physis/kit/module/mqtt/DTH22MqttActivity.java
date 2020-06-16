package com.physis.kit.module.mqtt;

import androidx.appcompat.app.AppCompatActivity;

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

public class DTH22MqttActivity extends PHYSIsMQTTActivity implements View.OnClickListener {

    private Button btnConnect;
    private EditText etSerialNum, etSubTopic;
    private TextView tvHumidity, tvTemperature;

    private String serialNum = null;
    private String subTopic = null;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth22_mqtt);

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
        showDTHValues(data);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_connect){
            connect();
        }
    }


    @SuppressLint("SetTextI18n")
    private void showDTHValues(String msg){
        String[] datas = msg.split(",");

        if(datas.length != 2)
            return;

        tvHumidity.setText(datas[0] + " %");
        tvTemperature.setText(datas[1] + " ℃");
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
        subTopic = etSubTopic.getText().toString();

        if(serialNum.length() != 12){
            Toast.makeText(getApplicationContext(), "PHYSIs Kit의 시리얼 넘버를 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(subTopic.length() == 0){
            Toast.makeText(getApplicationContext(), "Subscribe Topic을 확인하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isConnected){
            disconnectMQTT();
        }else{
            LoadingDialog.show(DTH22MqttActivity.this, "Connecting..");
            connectMQTT();
        }
    }

    private void init() {
        btnConnect = findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(this);

        etSerialNum = findViewById(R.id.et_serial_num);
        etSubTopic = findViewById(R.id.et_sub_topic);

        tvHumidity = findViewById(R.id.tv_humidity);
        tvTemperature = findViewById(R.id.tv_temperature);

        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();
    }

}
