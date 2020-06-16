#include <PHYSIs_Master.h>

#define CDS_PIN   A0

PHYSIs_BLE physisBLE;


void setup() {
  Serial.begin(115200);

  if (!physisBLE.enable()) {            // BLE 모듈 활성화
    while (1) {};
  }

}

void loop() {
  int cdsValue = analogRead(CDS_PIN);
  int cdsPercent = map(cdsValue, 0, 1023, 0, 100);

  physisBLE.sendMessage(String(cdsPercent));
}
