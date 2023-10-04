package Layer2_Dev;

import lejos.utility.Delay;

public class PushMotor {
    public static void push(){
        InitComp2.pushmotor.setSpeed(700);
        InitComp2.pushmotor.rotateTo(120,true);
        Delay.msDelay(800);
        InitComp2.pushmotor.rotateTo(-2);

    }
    public static void pushForward(){
     runDegs(-100,60);

     /*
     orState = dev2.psm.BBM2.isBusy()
            return motorState # returns true when motor busy
      */
    }
    public static void pushBack(){
        runDegs(110,60);
        /*
         motorState = dev2.psm.BBM1.isBusy()
            return motorState # returns true when motor busy, false when ended
         */
    }

    public static void  pushBackLight(){
        runDegs(80,30);
      /*  motorState = dev2.psm.BBM1.isBusy()
        return motorState # returns true when motor busy, false when ended
        */
    }




    public static void runDegs(int angle, int speed){
        InitComp2.pushmotor.setSpeed(speed);
        InitComp2.pushmotor.rotate(angle);
  }
}
