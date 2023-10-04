package Layer2_Dev;

import lejos.utility.Delay;

public class EjectMotor {

    public static void ejectForward(){
            InitComp2.ejectmotor.setSpeed(500);
            Delay.msDelay(1);
            InitComp2.ejectmotor.forward();
            Delay.msDelay(500);
            InitComp2.ejectmotor.stop();
            }
    public static void ejectForward1(){
        InitComp2.ejectmotor.setSpeed(500);
        Delay.msDelay(1);
        InitComp2.ejectmotor.forward();
        Delay.msDelay(600);
        InitComp2.ejectmotor.stop();
    } public static void ejectForward2(){
        InitComp2.ejectmotor.setSpeed(500);
        Delay.msDelay(1);
        InitComp2.ejectmotor.forward();
        Delay.msDelay(800);
        InitComp2.ejectmotor.stop();
    }
    public static void ejectBack() {
        InitComp2.ejectmotor.setSpeed(500);
        Delay.msDelay(1);
        InitComp2.ejectmotor.rotateTo(0);
    }
}
