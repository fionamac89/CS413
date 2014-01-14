//**************************************************************//
//  Name    : shiftIn Example 1.2                               //
//  Author  : Carlyn Maw                                        //
//  Date    : 25 Jan, 2007                                      //
//  Version : 1.0                                               //
//  Notes   : Code for using a CD4021B Shift Register    	//
//          :                                                   //
//****************************************************************

//define where your pins are
const int clockPin = 7;
const int shin_latchPin = 8;
const int shin_dataPin = 9;
const int shot_dataPin = 10;
const unsigned long lasts_time = 1000UL;

#define SHOT_TOTAL 9
#define nLEDs 3
#define DOWN HIGH
#define UP LOW

uint8_t ledBar[SHOT_TOTAL]; // Array representing LED PWM levels (byte size)
unsigned long timer[nLEDs];  //Use for time counting

//Define variables to hold the data 
//for shift register.
byte switchVar1 = 72;  //01001000

//define an array that corresponds to values for each 
//of the shift register's pins
char note2sing[] = {
  '0', '1', '2', '3', '4', '5', '6', '7'}; 


void setup() {
  //start serial
  Serial.begin(9600);

  //define pin modes
  pinMode(shin_latchPin, OUTPUT);
  pinMode(clockPin, OUTPUT);
  pinMode(shot_dataPin, OUTPUT); 
  pinMode(shin_dataPin, INPUT);

  // Initialize WS2803 - Clock needs to be low at least 600us to prepare itself.
  digitalWrite(clockPin, LOW);  //??? Does it affect shiftin
  delayMicroseconds(600);  //!!! Using delay

   // Initialize the ledBar array - 23% Brightness
   for(int i = 0; i < nLEDs; i++){
      ledBar[i] = 0x30;
    }
    for(int i = nLEDs; i < SHOT_TOTAL; i++){
      ledBar[i] = 0x00;
    }
    loadWS2803();  
  
  //Initialize timer
  for(int i = 0; i < nLEDs; i++){
    timer[i] = millis() -  lasts_time;
  }
}

void loop() {
   // Initialize the ledBar array - 23% Brightness
  //Pulse the latch pin:
  //set it to 1 to collect parallel data
  digitalWrite(shin_latchPin,1);
  //set it to 1 to collect parallel data, wait
  delayMicroseconds(20);
  
  //set it to 0 to transmit data serially  
  digitalWrite(shin_latchPin,0);

  //collect each shift register into a byte
  switchVar1 = shiftIn(shin_dataPin, clockPin);

  //This for-loop steps through the byte
  //bit by bit which holds the shift register data 
  //and if it was high (1) then it prints
  //the corresponding location in the array
  for (int n=0; n<=7; n++)
  {
    if (switchVar1 & (1 << n) ){
      if(note2sing[n] == '0' && ledBar[0] != 0x00){
        //Reset time counting
        timer[0] = millis();
        //Do sth to the No.0 led
        ledBar[0] = 0x00;  //Turn if OFF
        loadWS2803();
      }
      if(note2sing[n] == '1'){
        timer[1] = millis();
        //DO sth to the No.1 led
        ledBar[1] = 0x00;
        loadWS2803();        
      }
      if(note2sing[n] == '2'){
        timer[2] = millis();
        //DO sth to the No.1 led
        ledBar[2] = 0x00;
        loadWS2803();        
      }
      Serial.println(note2sing[n]);
    }
  }
  
  for(int i = 0; i < nLEDs; i++){
    if(millis() - timer[i] <  lasts_time){
      //  button was triggered within lasts_time
     }else{
       ledBar[i] = 0x30;  //Relight it.
    }
  }
  loadWS2803();        

}

//------------------------------------------------end main loop

void loadWS2803(){
    for (int wsOut = 0; wsOut < SHOT_TOTAL; wsOut++){
      shiftOut(shot_dataPin, clockPin, MSBFIRST, ledBar[wsOut]);
    }
    delayMicroseconds(600); // 600us needed to reset WS2803s !!! Using delay
}

////// ----------------------------------------shiftIn function
///// just needs the location of the data pin and the clock pin
///// it returns a byte with each bit in the byte corresponding
///// to a pin on the shift register. leftBit 7 = Pin 7 / Bit 0= Pin 0

byte shiftIn(int myDataPin, int myClockPin) { 
  int i;
  int temp = 0;
  int pinState;
  byte myDataIn = 0;

  pinMode(myClockPin, OUTPUT);
  pinMode(myDataPin, INPUT);
//we will be holding the clock pin high 8 times (0,..,7) at the
//end of each time through the for loop

//at the begining of each loop when we set the clock low, it will
//be doing the necessary low to high drop to cause the shift
//register's DataPin to change state based on the value
//of the next bit in its serial information flow.
//The register transmits the information about the pins from pin 7 to pin 0
//so that is why our function counts down
  for (i=7; i>=0; i--)
  {
    digitalWrite(myClockPin, 0);
    delayMicroseconds(0.2);
    temp = digitalRead(myDataPin);
    if (temp) {
      pinState = 1;
      //set the bit to 0 no matter what
      myDataIn = myDataIn | (1 << i);
    }
    else {
      //turn it off -- only necessary for debuging
     //print statement since myDataIn starts as 0
      pinState = 0;
    }

    //Debuging print statements
    //Serial.print(pinState);
    //Serial.print("     ");
    //Serial.println (dataIn, BIN);

    digitalWrite(myClockPin, 1);

  }
  //debuging print statements whitespace
  //Serial.println();
  //Serial.println(myDataIn, BIN);
  return myDataIn;
}

