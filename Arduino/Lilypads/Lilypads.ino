//**************************************************************//
//  Name    : LilyPads Control                                  //
//  Author  : Ziaki Wen                                         //
//  Date    : 14th, Jan. 2014                                   //
//  Version : 1.2                                               //
//  Notes   : Codes for switching on & off LEDs and             // 
//          : listening to pushbuttons at the same time      	//
//          :                                                   //
//****************************************************************

//define where your pins are 
const int clockPin = 7;
const int shin_latchPin = 8;
const int shin_dataPin = 9;
const int shot_dataPin = 10;

const unsigned long flip_time = 200UL;  //stay for 0.2 sec
const unsigned long lasts_time = 5000UL;  //stay for 5 sec

#define nLilies 4        //Number of Lilypads
#define SHOT_LEN 18 * nLilies  // Length of configuration array
#define nLeds 3          // Number of leds per lilypad !!! Using the RGB led, needs 3 pins per led
#define DOWN HIGH      // The button is pressed down
#define UP LOW         // The button is leased

uint8_t ledBar[SHOT_LEN]; // Array representing LED PWM xlevels (byte size)
byte switchVar1 = 72;  //hold the data for shift register; 01001000 for debug
int input = -1;  //Input from monitor; -1 for debug

boolean stop_flash = true;  //To control flashing pad
unsigned long flash_timers[nLilies];  //timers to control the flashing

unsigned long lasts_timers[nLilies];  //How long should the lilypad keeps lighting
unsigned long react_timers[nLilies];  //Use for calculating reaction time

void setup() {
  //start serial
  Serial.begin(115200);

  //define pin modes
  pinMode(shin_latchPin, OUTPUT);
  pinMode(clockPin, OUTPUT);
  pinMode(shot_dataPin, OUTPUT); 
  pinMode(shin_dataPin, INPUT);

  //Initialize timers
  for(int i = 0; i < nLilies; i++){
    react_timers[i] = millis();
    flash_timers[i] = millis();
    lasts_timers[i] = millis();
  }

  // Initialize WS2803 - Clock needs to be low at least 600us to prepare itself.
  digitalWrite(clockPin, LOW);
  delayMicroseconds(600);
  
  // set the light to twinkle
  stop_flash = false;
  
}

void loop() {
  if(!stop_flash){
     if (Serial.available() > 0){
        input = Serial.read();
        Serial.print("Not in game:");
        Serial.print(input);
        Serial.print('\n');
      if(input == '1'){
        stop_flash = true;
        for(int i = 0; i < nLilies; i++){
          setSameColor(i,0x00,0x00,0x00);
        }
      }
    }
    for(int i = 0; i < nLilies; i++){
      setPadFlash(i);
    }
    loadWS2803();
  }
  
  if(stop_flash){
      if (Serial.available() > 0){
        input = Serial.read();
        Serial.print("In the game:");
        Serial.print(input);
        Serial.print('\n');
      if(input >= '0' && input <= '4'){
        input = input - '0';
        setDiffColor(input,0x30,0x30,0x30);
        lasts_timers[input] = react_timers[input] = millis();
        loadWS2803();
      }else if(input == 'q'){
        // Quit the game mode
        stop_flash = false;
      }
    }
    //Check if Lilypads are expired
    for(int i = 0; i < nLilies; i++){
      if(isLightOn(i) && millis() - lasts_timers[i] > lasts_time){
        setSameColor(i,0x00,0x00,0x00);  //Turn it off
      }
    }
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
    for (int i = 0; i <= 7; i++){
      if (switchVar1 & (1 << i) ){
          if(isLightOn(i)){
            //Do sth to the No.i led
            Serial.print(i);
            Serial.print(", ");
            Serial.print(millis() - react_timers[i]);
            Serial.print("\n");
            setSameColor(i,0x00,0x00,0x00);
         }
      }
    }
    loadWS2803();
  
  }

}

//------------------------------------------------end main loop

//----------------------------------- Specialized LED functions
///// Assume that each lilypad has three rgb leds, also when one light on the pad is on then all are on.
///// isLightOn Function
boolean isLightOn(int pos_pad){
  return getRGBRed(pos_pad,0) != 0x00 || getRGBGreen(pos_pad,0) != 0x00 || getRGBBlue(pos_pad,0) != 0x00;
}

///// setPadFlash Function
///// set the corresponding pad to twinkle
///// depends on a global boolean: STOPFLASH;
void setPadFlash(int pos_pad){
  if(pos_pad < nLilies && stop_flash == false){
    if(millis() - flash_timers[pos_pad] > flip_time){
      if(isLightOn(pos_pad)){
        // the pad is on, switch it off
        setSameColor(pos_pad,0x00, 0x00, 0x00);
      }else{
        setSameColor(pos_pad, 0x20, 0x30, 0x30);
      }
      flash_timers[pos_pad] = millis();
    }
  }
}

///// setSameColor Function
///// Lit all leds on the same pad with same coour scheme
///// Parameters only to control the brightness
void setSameColor(int pos_pad, byte red, byte green, byte blue){
  if(pos_pad < nLilies){
    for(int i = 0; i < nLeds; i++){
      setRGBLed(pos_pad,i,red,green,blue);
    }
  }else{
    for(int i = 0; i < nLeds; i++){
      Serial.println("Error: out of boundary @ setDiffColor.");
      setRGBLed(pos_pad,i,0x00,0x00,0x00);      
    } 
  }
}

///// setDiffColor Function
///// Light 1st with red, 2nd with green and the last with blue colours
///// Parameters only to control the brightness
void setDiffColor(int pos_pad, byte red, byte green, byte blue){
  if(pos_pad < nLilies){
    setRGBLed(pos_pad,0,red,0x00,0x00);
    setRGBLed(pos_pad,1,0x00,green,0x00);
    setRGBLed(pos_pad,2,0x00,0x00,blue);
  }else{
    for(int i = 0; i < nLeds; i++){
      Serial.println("Error: out of boundary @ setDiffColor.");
      setRGBLed(pos_pad,i,0x00,0x00,0x00);      
    } 
  }  
}

//----------------------------------- general LED functions
///// Return LED colour information
byte getRGBRed(int pos_pad, int pos_led){
  if(pos_pad >= nLilies && pos_led >= nLeds){
    Serial.println("Error: out of boundary @ getRGBRed.");
    return 0x00;
  }
  return ledBar[pos_pad * 18 + pos_led * 3];
}

byte getRGBGreen(int pos_pad, int pos_led){
  if(pos_pad >= nLilies && pos_led >= nLeds){
    Serial.println("Error: out of boundary @ getRGBGreen.");
    return 0x00;
  }
  return ledBar[pos_pad * 18 + pos_led * 3 + 1];
}

byte getRGBBlue(int pos_pad, int pos_led){
  if(pos_pad >= nLilies && pos_led >= nLeds){
    Serial.println("Error: out of boundary @ getRGBBlue.");
    return 0x00;
  }
  return ledBar[pos_pad * 18 + pos_led * 3 + 2];
}

///// setRGBLed function
///// Set the Colour & Brightness of an RGB led
///// !!! It depends to ledBar[] array
void setRGBLed(int pos_pad, int pos_led, byte red, byte green, byte blue){
  if(pos_pad < nLilies && pos_led < nLeds){
    int pos_bar = pos_pad * 18 + pos_led * 3;
    ledBar[pos_bar++] = red;
    ledBar[pos_bar++] = green;
    ledBar[pos_bar++] = blue;  
  }else{
    Serial.println("Error: out of boundary @ setRGBLed.");
  }
}

////// ----------------------------------------load shiftout function
///// By calling this function, you can override the shiftout with 
///// the latest digits in ledBar
void loadWS2803(){
    for (int wsOut = 0; wsOut < SHOT_LEN; wsOut++){
      shiftOut(shot_dataPin, clockPin, MSBFIRST, ledBar[wsOut]);
    }
    delayMicroseconds(600);
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

