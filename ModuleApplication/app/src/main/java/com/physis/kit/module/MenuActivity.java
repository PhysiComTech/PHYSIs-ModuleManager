package com.physis.kit.module;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.physis.kit.module.ble.*;
import com.physis.kit.module.dialog.MenuDialog;
import com.physis.kit.module.mqtt.BuzzerMqttActivity;
import com.physis.kit.module.mqtt.DTH22MqttActivity;
import com.physis.kit.module.mqtt.IlluminationMqttActivity;
import com.physis.kit.module.mqtt.LedButtonMqttActivity;
import com.physis.kit.module.mqtt.RelayMqttActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getSimpleName();
    private static final int MODULE_DTH22 = 11;
    private static final int MODULE_ILLUMINATION = 12;
    private static final int MODULE_BUZZER = 13;
    private static final int MODULE_LED_BUTTON = 14;
    private static final int MODULE_RELAY = 15;

    private static final int COMMUNICATION_WIFI = 24;
    private static final int COMMUNICATION_BLE = 25;

    private MenuDialog communicationDialog = null;

    private int moduleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
    }

    @Override
    public void onClick(View v) {
        int tag = Integer.parseInt(v.getTag().toString());
        if(v.getId() == R.id.btn_ble || v.getId() == R.id.btn_wifi){
            Intent intent = tag == COMMUNICATION_WIFI ?
                    getMQTTModuleIntent() : getBLEModuleIntent();
            startActivity(intent);
            communicationDialog.dismiss();
        }else{
            moduleType = tag;
            showSelectedCommunicationDialog();
        }
    }

    private Intent getMQTTModuleIntent(){
        switch (moduleType){
            case MODULE_DTH22:
                return new Intent(this, DTH22MqttActivity.class);
            case MODULE_ILLUMINATION:
                return new Intent(this, IlluminationMqttActivity.class);
            case MODULE_BUZZER:
                return new Intent(this, BuzzerMqttActivity.class);
            case MODULE_LED_BUTTON:
                return new Intent(this, LedButtonMqttActivity.class);
            case MODULE_RELAY:
                return new Intent(this, RelayMqttActivity.class);
            default:
                return null;
        }
    }

    private Intent getBLEModuleIntent(){
        switch (moduleType){
            case MODULE_DTH22:
                return new Intent(this, DTH22BleActivity.class);
            case MODULE_ILLUMINATION:
                return new Intent(this, IlluminationBleActivity.class);
            case MODULE_BUZZER:
                return new Intent(this, BuzzerBleActivity.class);
            case MODULE_LED_BUTTON:
                return new Intent(this, LedButtonBleActivity.class);
            case MODULE_RELAY:
                return new Intent(this, RelayBleActivity.class);
            default:
                return null;
        }
    }

    private void showSelectedCommunicationDialog() {
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_select_communication, null);
        final Button btnWifi = view.findViewById(R.id.btn_wifi);
        btnWifi.setTag(COMMUNICATION_WIFI);
        final Button btnBle = view.findViewById(R.id.btn_ble);
        btnBle.setTag(COMMUNICATION_BLE);
        btnWifi.setOnClickListener(this);
        btnBle.setOnClickListener(this);
        communicationDialog = new MenuDialog();
        communicationDialog.show(MenuActivity.this, null, view, "닫기");
    }

    private void init() {
        AnimationDrawable animDrawable = (AnimationDrawable) findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();

        Button btnDTH22 = findViewById(R.id.btn_dht22);
        btnDTH22.setTag(MODULE_DTH22);
        btnDTH22.setOnClickListener(this);

        Button btnIllumination = findViewById(R.id.btn_illumination);
        btnIllumination.setTag(MODULE_ILLUMINATION);
        btnIllumination.setOnClickListener(this);

        Button btnBuzzer = findViewById(R.id.btn_buzzer);
        btnBuzzer.setTag(MODULE_BUZZER);
        btnBuzzer.setOnClickListener(this);

        Button btnLEDButton = findViewById(R.id.btn_led_button);
        btnLEDButton.setTag(MODULE_LED_BUTTON);
        btnLEDButton.setOnClickListener(this);

        Button btnRelay = findViewById(R.id.btn_relay);
        btnRelay.setTag(MODULE_RELAY);
//        btnRelay.setOnClickListener(this);
    }

}
