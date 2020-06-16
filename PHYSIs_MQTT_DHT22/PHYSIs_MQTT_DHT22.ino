#include <PHYSIs_Master.h>
#include <DHT.h>

#define DHT_PIN   3
#define DHT_TYPE  DHT22

DHT dht(DHT_PIN, DHT_TYPE);
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

  dht.begin();
}

void loop() {
  float humidity = dht.readHumidity();
  float temperature = dht.readTemperature();
  if (isnan(humidity) || isnan(temperature)) {
    Serial.println("DHT sensor error!");
    return;
  }

  String sendData = String(humidity) + "," + String(temperature);
  physisWiFi.publish(SERIAL_NUMBER, PUB_TOPIC, sendData);
  
  physisWiFi.startReceiveMsg();
}
