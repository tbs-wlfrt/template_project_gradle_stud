package RobtArm;




import lejos.utility.Delay;

public class HorizontaleArm {

    public static void resetPos(int speed) {
        Delay.msDelay(1);
        initComp.motor.rotateTo(0);
    }

    public static void goDown(int speed) {
        initComp.motor.setSpeed(speed);
        Delay.msDelay(1);

        initComp.motor.rotateTo(85);
    }
    public static void goUp(int speed) {
        initComp.motor.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor.rotateTo(0);
    }


}