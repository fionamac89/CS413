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

const unsigned long flip_time = 400UL;  //stay for 0.4 sec
const unsigned long lasts_time = 4000UL;  //stay for 4 sec
const unsigned long start_time = 10000UL;  // stay for 1 sec  TODO back to 1
const unsigned long add_time = 500UL;  //add another 0.5 sec

#define nLilies 4        //Number of Lilypads
#define SHOT_LEN 18 * nLilies  // Length of configuration array
#define nLeds 3          // Number of leds per lilypad !!! Using the RGB led, needs 3 pins per led
#define DOWN HIGH      // The button is pressed down
#define UP LOW         // The button is leased
#define ROUND 5        //Rounds of game
#define BRIGHT 0x05

// Define game modes
enum games{
  ready_g,  //Not in any game modes
  rand_g,   //In Random game mode
  simon_g,  //In SimonSays game mode
  comp_g    //Competative game mode
};

uint8_t ledBar[SHOT_LEN]; // Array representing LED PWM xlevels (byte size)
byte switchVar1 = 72;  //hold the data for shift register; 01001000 for debug
int input = -1;  //Input from monitor; -1 for debug
int count = -1;  // count the number of passed rounds
int len_simon = -1; // number of buttons supposed to be hit
int num_hit = -1;  // times when buttons are hit
boolean block = false;  //block the buttons side
games now_mode = ready_g;  //Stores the current game mode

unsigned long flash_timers[nLilies];  //timers to control the flashing
unsigned long lasts_timers[nLilies];  //How long should the lilypad keeps lighting
unsigned long react_timers[nLilies];  //Use for calculating reaction time
unsigned long play_timer;  //Play timer for SimonSays

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
  now_mode = ready_g;
}

void loop() {
  
  // Ready mode
  if(now_mode == ready_g){
    
    for(int i = 0; i < nLilies; i++){
      setPadFlash(i);
    }

     if (Serial.available() > 0){
        input = Serial.read();
        Serial.print("Not in game:");
        Serial.print(input);
        Serial.print('\n');
      if(input == 'd'){
        now_mode = rand_g;
        silence();
      }else if(input == 's'){
        now_mode = simon_g;
        silence();
        count = 0;
        block = true;
        len_simon = 0;
      }
    }
    
    loadWS2803();
  }
  
  // random game
  if(now_mode == rand_g){
    
      // Listen to back-end instructions
      if (Serial.available() > 0){
        input = Serial.read();
        Serial.print("Random game:");
        Serial.print(input);
        Serial.print('\n');
      if(input >= '0' && input <= '3'){
        input = input - '0';
        setDiffColor(input,BRIGHT,BRIGHT,BRIGHT);
        lasts_timers[input] = react_timers[input] = millis();
        loadWS2803();
      }else if(input == 'q'){
        // Quit the game mode
        Serial.print("Quit Game. \n");
        now_mode = ready_g;
      }else if(input == 's'){
        now_mode = simon_g;
        silence();
        count = 0;
        block = true;
        len_simon = 0;
        Serial.print("To simonSays. \n");
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

  //SimonSays mode
  if(now_mode == simon_g){
    while(count < ROUND){
      
      // Listen to back-end instructions
      if (Serial.available() > 0){
        input = Serial.read();
        Serial.print("SimonSays game:");
        Serial.print(input);
        Serial.print('\n');
      if(input == 'c'){  //continue to the next round
        count++;
        len_simon = 0;
        block = true;
      }else if(input == 'p' && block){  //Inputs stopped, unblock button listeners
        startGame();
        num_hit = 0;
        block = false;
        play_timer = millis();
      }else if(input == 'q'){
        Serial.print("Quit SimonSays \n");
        // Quit the game mode
        now_mode = ready_g;
        break;
      }else if(input == 'd'){
        Serial.print("To Random Game \n");
        now_mode = rand_g;
        silence();
        loadWS2803();
        break;
      }else if(input >= '0' && input <= '3'){
        input = input - '0';
        len_simon++;
        //flip-flop the pad's light
        flipflop(input);
      }
    }else{ 
      if((millis() - play_timer < start_time + count * add_time) && !block
          && num_hit < len_simon){
        //Serial.println("Listening to buttons");
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
            num_hit++;
            Serial.print("Hit: ");
            Serial.print(i);
            Serial.print('\n');
            flipflop(i);
          }
        }
       }
      } 
    }
    if(count == ROUND){
      // Won the game
      Serial.println("You won the game!");
      wonGame();
      delay(2000);//!!!! DELAY
      now_mode = ready_g;
    }
  }
}

//------------------------------------------------end main loop

//----------------------------------- Specialized LED functions
///// wonGame Function
///// Green all lights once
void wonGame(){
   for(int i = 0; i < nLilies; i++){
     setSameColor(i,0x00,BRIGHT,0x00);
   }
   loadWS2803();
   delay(1000);  //!!! warning: DELAY
   for(int i = 0; i < nLilies; i++){
     setSameColor(i,0x00,0x00,0x00);
   }
   loadWS2803();
}

///// startGame Function
///// Goes from inner to out, red -> yellow -> green
void startGame(){
   for(int i = 0; i < nLilies; i++){
     setRGBLed(i,0, BRIGHT,0x00,0x00);
   }
   loadWS2803();
   delay(400);  //!!! warning: DELAY
   for(int i = 0; i < nLilies; i++){
     setRGBLed(i,0, 0x00,0x00,0x00);
     setRGBLed(i,1, BRIGHT,BRIGHT,0x00);
   }
   loadWS2803();
   delay(400);  //!!! warning: DELAY
   for(int i = 0; i < nLilies; i++){
     setRGBLed(i,1,0x00,0x00,0x00);
     setRGBLed(i,2,0x00,BRIGHT,0x00);
   }
   loadWS2803();
   delay(400);  //!!! warning: DELAY
   for(int i = 0; i < nLilies; i++){
     setRGBLed(i,2,0x00,0x00,0x00);     
   }
   loadWS2803();   
}

///// silence Function
///// Turn off all lights, initialisation before starting the game mode
void silence(){
  for(int i = 0; i < nLilies; i++){
    setSameColor(i,0x00,0x00,0x00);
  }
}

///// Assume that each lilypad has three rgb leds, also when one light on the pad is on then all are on.
///// isLightOn Function
boolean isLightOn(int pos_pad){
  return getRGBRed(pos_pad,0) != 0x00 || getRGBGreen(pos_pad,0) != 0x00 || getRGBBlue(pos_pad,0) != 0x00;
}
///// filp-flop the pad lights
void flipflop(int pos_pad){
 setDiffColor(pos_pad,BRIGHT,BRIGHT,BRIGHT);
 loadWS2803();
 delay(200);  //!!! warning: DELAY
 setDiffColor(pos_pad,0x00,0x00,0x00);
 loadWS2803();
}

///// setPadFlash Function
///// set the corresponding pad to twinkle
///// depends on a global boolean: STOPFLASH;
void setPadFlash(int pos_pad){
  if(pos_pad < nLilies && now_mode == ready_g){
    if(millis() - flash_timers[pos_pad] > flip_time){
      if(isLightOn(pos_pad)){
        // the pad is on, switch it off
        setSameColor(pos_pad,0x00, 0x00, 0x00);
      }else{
        setSameColor(pos_pad, BRIGHT, BRIGHT, BRIGHT);
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
    ledBar[pos_bar++] = blue;
    ledBar[pos_bar++] = green;
    ledBar[pos_bar++] = red;  
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

