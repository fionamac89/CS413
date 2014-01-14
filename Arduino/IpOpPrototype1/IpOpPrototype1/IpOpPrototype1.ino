int incomingByte = 0; 
int ledPin = 2;
int ledPinState = 0;
unsigned long timeCount= millis();
int buttonPin = 13;
int buttonState = 0;

void setup(){
 Serial.begin(4800); 
}

void loop() {
  
    if (Serial.available() > 0){
        incomingByte = Serial.read();
        ledPinState=1;
        digitalWrite(ledPin, HIGH);
        timeCount = millis();
        
    }  
    buttonState = digitalRead(buttonPin);

    if(buttonState == 1&& ledPinState==1){
       Serial.print("1, ");
       Serial.print(millis() - timeCount);
       Serial.print("\n");
       digitalWrite(ledPin, LOW);
       ledPinState=0;
  }

}




