package RobtArm;

import lejos.utility.Delay;

public class GripperMotor {
   // static final EV3MediumRegulatedMotor motor3 = new EV3MediumRegulatedMotor(MotorPort.C);
public static void colosed(int speed) {
    initComp.motor3.setSpeed(speed);
    Delay.msDelay(1);
    initComp.motor3.rotateTo(40,true);
}
    public static void open(int speed) {
        initComp.motor3.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor3.rotateTo(0);
    }
    public static void resetPos() {
        Delay.msDelay(1);
        initComp.motor3.rotateTo(0);
    }
}
