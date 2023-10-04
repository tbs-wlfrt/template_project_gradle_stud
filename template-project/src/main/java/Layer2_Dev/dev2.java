package Layer2_Dev;

import lejos.utility.Delay;

public class dev2 {
    public static void pushproduct() {
       PushMotor.push();

    }

    public static void PressDown() {
        PressMotor.pressDown();
    }
    public static void PressUp() {
        PressMotor.pressUp();
    }
    public static void PressDown1() {
        PressMotor.pressDown1();
    }
    public static void PressUp1() {
        PressMotor.pressUp1();
    }
    public static void Mediumpress() {
        PressMotor.mediumpress();
    }
    public static void Inialposition() {

           PressMotor.resetposition();
    }
    public static boolean ispressed() {
        return TouchSensor.press();
    }
    public static void Eject() {
      //  EjectMotor.ejectForward();
       // Delay.msDelay(2000);
        EjectMotor.ejectForward();
        Delay.msDelay(500);
        EjectMotor.ejectBack();
        Delay.msDelay(1000);
        EjectMotor.ejectForward1();
        Delay.msDelay(1000);
        EjectMotor.ejectBack();
        Delay.msDelay(1000);
        EjectMotor.ejectForward2();
        Delay.msDelay(800);
        EjectMotor.ejectBack();
    }
  }
