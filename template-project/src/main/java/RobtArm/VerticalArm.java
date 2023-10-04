package RobtArm;

import lejos.utility.Delay;


public class VerticalArm {


    public static void turnright1(int speed) {
            initComp.motor1.setSpeed(speed);
            Delay.msDelay(1);
        initComp.motor1.rotateTo(88);
    }
    public static void turnright2(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(145);
    }
    public static void turnright3(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
    }
    public static void turnright4(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(180);
    }
    public static void resetPos(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(0);
    }
    public static void turnleft1(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotate(87);
    }
    public static void turnleft2(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(127);
    }
    public static void turnleft3(int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(180);
    }
    public static void turnleft4 (int speed) {
        initComp.motor1.setSpeed(speed);
        Delay.msDelay(1);
        initComp.motor1.rotateTo(238);
    }
public static void tur(int speed) {
    initComp.motor1.setSpeed(speed);
}
public static void getpos() {
    initComp.motor1.rotateTo(0);
}


/**
 Movement to the bag
                       **/

//drop product to bag

public static void dropproduct(int speed) {
    initComp.motor1.setSpeed(speed);
    Delay.msDelay(1);
    initComp.motor1.rotateTo(285);
}

}
