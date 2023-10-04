package Agents;

import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class RunLayer1 {

    public static void main(String[] args) throws Exception {
           EV3MediumRegulatedMotor m= new EV3MediumRegulatedMotor(MotorPort.B);
         //  m.setSpeed(200);
       // Delay.msDelay(1);

        //m.suspendRegulation();
        m.resetTachoCount();
        m.setSpeed(500);
        m.brake();


        Delay.msDelay(10);
        m.rotateTo(-80,false);
        Delay.msDelay(4);
        m.rotateTo(0,false);

    //    //  m.rotateTo(0);
    }
}