package Layer1_Dev;

import lejos.utility.Delay;

import static Agents.ShredAgent.restart;

public class ShredderMotor {
    public static void runShredermotorstart(int speed){

        InitComp.shredermotor.setSpeed(speed);
        Delay.msDelay(5);
        InitComp.shredermotor.forward();
    }
    public static void stopShredder() {
        InitComp.shredermotor.stop();
    }
    public static void re_runShredermotor(int speed){
if (restart){
        InitComp.shredermotor.setSpeed(speed);
        Delay.msDelay(5);
        InitComp.shredermotor.forward();
    }
}
}
