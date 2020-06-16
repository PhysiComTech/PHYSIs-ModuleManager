#include <PHYSIs_Master.h>
#include <DHT.h>

#define DHT_PIN   3
#define DHT_TYPE  DHT22

DHT dht(DHT_PIN, DHT_TYPE);
PHYSIs_BLE physisBLE;


void setup() {
  Serial.begin(115200);

  if (!physisBLE.enable()) {            // BLE 모듈 활성화
    while (1) {};
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
  physisBLE.sendMessage(sendData);
}
