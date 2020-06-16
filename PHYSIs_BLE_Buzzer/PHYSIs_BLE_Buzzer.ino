#include <PHYSIs_Master.h>

#define BUZZER_PIN  3

PHYSIs_BLE physisBLE;


void setup() {
  Serial.begin(115200);

  if (!physisBLE.enable()) {            // BLE 모듈 활성화
    while (1) {};
  }

  physisBLE.messageListener = &messageListener;

  pinMode(BUZZER_PIN, OUTPUT);
}

void loop() {
  physisBLE.startReceiveMsg();
}


void messageListener(String msg) {
  Serial.print(F("# Receive Message : "));
  Serial.println(msg);

  if (msg.equals("ON")) {
    digitalWrite(BUZZER_PIN, HIGH);
  } else if (msg.equals("OFF")) {
    digitalWrite(BUZZER_PIN, LOW);
  } else{
    analogWrite(BUZZER_PIN, msg.toInt());
  }

}
