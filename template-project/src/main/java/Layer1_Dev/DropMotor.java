package Layer1_Dev;

import lejos.utility.Delay;

public class DropMotor {

    public static void resetPosition(int speed ,int degre) {
        InitComp.dropmotor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp.dropmotor.rotateTo(degre);    }
    public static void dropup(){
        InitComp.dropmotor.rotate(360);
    }
    public static void dropdown(){
        InitComp.dropmotor.rotateTo(0);
    }

}
