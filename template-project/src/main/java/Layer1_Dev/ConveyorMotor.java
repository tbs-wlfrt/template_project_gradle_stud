package Layer1_Dev;

import lejos.utility.Delay;

import java.rmi.RemoteException;

import static Agents.ShredAgent.restart;

public class ConveyorMotor {

   public static void runconveyor1(int speed) {
    //  if (!InitComp.motor1.isStalled()){
       InitComp.motor1.setSpeed(speed);
       Delay.msDelay(10);
       InitComp.motor1.brake();
       Delay.msDelay(10);
       InitComp.motor1.forward();}
     /* else {
          InitComp.motor1.setSpeed(0);
          Delay.msDelay(10);
          InitComp.motor1.brake();
          Delay.msDelay(10);
          InitComp.motor1.backward();
          Delay.msDelay(500);
          InitComp.motor1.stop();
      }

   }*/
     public static void re_runconveyor1(int speed) {
           if (restart) {
               InitComp.motor1.setSpeed(speed);
               Delay.msDelay(10);
               InitComp.motor1.brake();
               Delay.msDelay(10);
               InitComp.motor1.forward();
           }  }
    public static void Stopconveyor1() {
        InitComp.motor1.stop();
    }
    public static void emergencyStop() {
        InitComp.motor1.setSpeed(0);
    }
    public static void runDegsconvoyer1(int angle, int speed){
        InitComp.motor1.setSpeed(speed);
        InitComp.motor1.rotate(angle);
    }
    public static void runSecsconvoyer1(int angle ) {
        InitComp.motor1.rotate(angle);
    }
    public static boolean isStalled() throws RemoteException {
     return InitComp.motor.isStalled() ;
    }
    public static void solvestucking() {
       InitComp.motor.stop();
        Delay.msDelay(10);
        InitComp.motor1.setSpeed(400);
        Delay.msDelay(1);
        InitComp.motor1.backward();
        Delay.msDelay(50);
        InitComp.motor.stop();
    }
}
