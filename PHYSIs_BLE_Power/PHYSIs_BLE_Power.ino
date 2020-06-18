#include <PHYSIs_Master.h>

#define POWER_PIN  3

PHYSIs_BLE physisBLE;


void setup() {
  Serial.begin(115200);

  if (!physisBLE.enable()) {            // BLE 모듈 활성화
    while (1) {};
  }

  physisBLE.messageListener = &messageListener;

  pinMode(POWER_PIN, OUTPUT);
}

void loop() {
  physisBLE.startReceiveMsg();
}


void messageListener(String msg) {
  Serial.print(F("# Receive Message : "));
  Serial.println(msg);

  if (msg.equals("ON")) {
    digitalWrite(POWER_PIN, HIGH);
  } else if (msg.equals("OFF")) {
    digitalWrite(POWER_PIN, LOW);
  } else{
    analogWrite(POWER_PIN, msg.toInt());
  }

}
