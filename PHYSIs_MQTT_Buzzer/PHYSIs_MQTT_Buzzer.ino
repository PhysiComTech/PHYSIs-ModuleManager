#include <PHYSIs_Master.h>

#define BUZZER_PIN  3

PHYSIs_WiFi physisWiFi;

const String WIFI_SSID     = "MDB";               // WiFi 명
const String WIFI_PWD      = "12345678";          // WiFi 비밀번호

const String SERIAL_NUMBER = "XXXXXXXXXXXX";      // PHYSIs KIT 시리얼번호
const String SUB_TOPIC     = "CON";             // Subscribe Topic

void setup() {
  Serial.begin(115200);

  physisWiFi.enable();                             // WiFi 모듈 활성화

  physisWiFi.connectWiFi(WIFI_SSID, WIFI_PWD);     // 지정 WiFi에 대한 연결 요청
  Serial.print("# WiFi Connecting..");
  delay(1000);

  while (!physisWiFi.isWiFiStatus()) {             // WiFi가 연결될때 까지 반복해서 연결상태를 확인
    Serial.print(".");
    delay(1000);
  }
  Serial.println(F("Connected..."));


  Serial.print("# MQTT Connecting..");
  if (physisWiFi.connectMQTT()) {                  // PHYSIs 플랫폼의 MQTT Broker와 연결
    Serial.println("Success...");
    if (physisWiFi.startSubscribe(SERIAL_NUMBER, SUB_TOPIC)) {      // Subscribe 요청
      Serial.print(">> Start Subscirbe : ");
      Serial.println(SUB_TOPIC);
    }
  } else {
    Serial.print("Fail...");
  }

  physisWiFi.subscribeListener = &subscribeListener;


  pinMode(BUZZER_PIN, OUTPUT);
}

void loop() {
  physisWiFi.startReceiveMsg();
}

void subscribeListener(String serialNum, String topic, String message) {
  if (!serialNum.equals(SERIAL_NUMBER) || !topic.equals(SUB_TOPIC))
    return;
    
  if (message.equals("ON")) {
    digitalWrite(BUZZER_PIN, HIGH);
  } else if (message.equals("OFF")) {
    digitalWrite(BUZZER_PIN, LOW);
  } else {
    analogWrite(BUZZER_PIN, msg.toInt());
  }
}
