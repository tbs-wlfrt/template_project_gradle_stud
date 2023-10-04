package RobtArm;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class initComp {
     static EV3LargeRegulatedMotor motor1 = null;
     static EV3LargeRegulatedMotor motor = null;
     static EV3MediumRegulatedMotor motor3 = null;
    static EV3ColorSensor color = null;



    public static void initComponents(){

        motor1 = new EV3LargeRegulatedMotor(MotorPort.A);
        motor = new EV3LargeRegulatedMotor(MotorPort.B);
        motor3 = new EV3MediumRegulatedMotor(MotorPort.C);
        color= new EV3ColorSensor(SensorPort.S1);
      //  System.out.println("pos"+motor1.getTachoCount());

    }

}
