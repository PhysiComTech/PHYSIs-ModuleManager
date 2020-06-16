#include <PHYSIs_Master.h>

#define CDS_PIN   A0

PHYSIs_WiFi physisWiFi;

const String WIFI_SSID     = "MDB";               // WiFi 명
const String WIFI_PWD      = "12345678";          // WiFi 비밀번호

const String SERIAL_NUMBER = "XXXXXXXXXXXX";      // PHYSIs KIT 시리얼번호
const String PUB_TOPIC     = "STATE";             // Subscribe Topic


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
  } else {
    Serial.print("Fail...");
  }
}

void loop() {
  int cdsValue = analogRead(CDS_PIN);
  int cdsPercent = map(cdsValue, 0, 1023, 0, 100);
  
  physisWiFi.publish(SERIAL_NUMBER, PUB_TOPIC, String(cdsPercent));

  physisWiFi.startReceiveMsg();
}
