#include <SoftwareSerial.h>

SoftwareSerial BLU(0, 1);

int redPin = 6;
int greenPin = 5;
int bluePin = 4;

void setup()
{
  Serial.begin(9600);
  BLU.begin(9600);

  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  // analogWrite(redPin, 255);
  // analogWrite(greenPin, 255);
  // analogWrite(bluePin, 255);
  Serial.write("ON\n");
}

void loop()
{
  if (BLU.available() > 0)
  {
    String string="";
    int input[3];
    int i=0;
    

    while (BLU.available() > 0)
    {
      char command = ((byte)BLU.read());
      if (command == ';')
      {
        input[i]=string.toInt();
        break;
      }
      if (command=='.')
      {
        input[i]=string.toInt();
        string="";
        i++;
      }
      else
      {
        string += command;
      }
      delay(1);
    }

    int redInt = input[0];
    int greenInt = input[1];
    int blueInt = input[2];

    analogWrite(redPin, redInt);
    analogWrite(greenPin, greenInt);
    analogWrite(bluePin, blueInt);

    Serial.print("Red: ");
    Serial.print(redInt);
    Serial.print(" Green: ");
    Serial.print(greenInt);
    Serial.print(" Blue: ");
    Serial.print(blueInt);
    Serial.println();
    
    delay(10);
  }
}
