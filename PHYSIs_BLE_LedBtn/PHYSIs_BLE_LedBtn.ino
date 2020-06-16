#include <PHYSIs_Master.h>

#define BTN_PIN   2
#define LED_PIN   3

PHYSIs_BLE physisBLE;

void setup() {
  Serial.begin(115200);

  if (!physisBLE.enable()) {            // BLE 모듈 활성화
    while (1) {};
  }

  pinMode(BTN_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);

  physisBLE.messageListener = &messageListener;

}

void loop() {
  
  physisBLE.sendMessage(String(digitalRead(BTN_PIN)));
  delay(50);
  
  physisBLE.startReceiveMsg();
}




void messageListener(String msg) {
  Serial.print(F("# Receive Message : "));
  Serial.println(msg);
  if (msg.equals("ON")) {
    digitalWrite(LED_PIN, HIGH);
  } else if (msg.equals("OFF")) {
    digitalWrite(LED_PIN, LOW);
  }
}
