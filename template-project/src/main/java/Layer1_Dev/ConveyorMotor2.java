package Layer1_Dev;

import Layer2_Dev.InitComp2;
import lejos.utility.Delay;

class Conveyormotor {

    public static void GoGreenPosition(int speed) {
        InitComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.motor.backward();
        Delay.msDelay(950);
        InitComp.motor.stop();
        InitComp.motor.getPosition();
    }
    public static void GoBluePosition(int speed) {
        InitComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.motor.backward();
        Delay.msDelay(650);
        InitComp.motor.stop();
        InitComp.motor.getPosition();
    }
    public static void Stopconveyor2() {
        InitComp.motor.stop();
    }
    public static void GoYelloPosition( int speed){
        InitComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.motor.backward();
        Delay.msDelay(1250);
        InitComp.motor.stop();
        InitComp.motor.getPosition();
    }
    public static void GoRedPosition(int speed) {
        InitComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.motor.backward();
        Delay.msDelay(3000);
        InitComp.motor.stop();
        InitComp.motor.getPosition();
    }
    public static void runconvoyer(int speed) {
        InitComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.motor.rotateTo(-500);
    }
    //////////////
    /*
    public static void start(int motorSpeed){
        motor1.setSpeed(100);

        System.out.println("Convoyer Started");
    }
    public static void startSlaow(int motorSpeed){
        motor1.setSpeed(20);

        System.out.println("Convoyer Slow Started");
    }
        public static void Stop() {
        motor1.stop();
    }



    public static void runDegs(int angle, int speed){
          motor1.setSpeed(speed);
   //     Delay.msDelay(500);
          motor1.rotate(angle);
         // motor1.rotateTo();
*/
    }
    //---------------
    /* def brickStucked(self):
            dev1.psm.BBM2.runSecs(2, -100, True)
            print(f'Brick stucked')


        def runDegs(self,degree,speed):
            dev1.psm.BBM2.runDegs(degree, speed, True, False)
            motorState = dev1.psm.BBM2.isBusy()
            print(f'Motor rotated {degree} degree on {speed} speed')
            return motorState # returns true when motor busy

     */

