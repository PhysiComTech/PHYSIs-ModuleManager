# PHYSIs Module Manager
- [ PHYSIs 메이커 보드 ]를 기반으로 센서모듈과 애플리리케이션 간의 원격 제어 및 모니터링 기능을 지원
===
** 프로젝트 항목
- ModuleApplication 
: 센서모듈 연결 및 제어를 위한 안드로이드 애플리케이션 
- PHYSIs_BLE_Buzzer & PHYSIs_MQTT_Buzzer
: 애플리리케이션에서 수신된 명령에 따라 부저 센서에 대한 소리 출력을 제어하기 위한 아두이노 소스코드 
- PHYSIs_BLE_CDS & PHYSIs_MQTT_CDS
: 아날로그 조도 센서를 통해 현재 빛의 세기를 측정하고 애플리리케이션으로 전송하는 아두이노 소스코드
- PHYSIs_BLE_DHT22 & PHYSIs_MQTT_DHT22
: DHT22 센서를 통해 현재의 온도와 습도값을 측정하고 애플리리케이션으로 전송하는 아두이노 소스코드
- PHYSIs_BLE_LedBtn & PHYSIs_MQTT_LedBtn
: 애플리리케이션에서 수신된 명령에 따라 LED 전원을 제어하고 버튼 상태정보를 전송하는 아두이노 소스코드
===
* 활용하고자하는 통신방식에 따라 프로젝트를 선택하여 사용
