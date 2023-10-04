package Layer2_Dev;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class InitComp2 {
    static EV3MediumRegulatedMotor pushmotor = null;
    static EV3LargeRegulatedMotor pressmotor = null;
    static EV3LargeRegulatedMotor ejectmotor = null;
    public static EV3LargeRegulatedMotor conveyor = null;

    public static EV3TouchSensor touchSensor = null;
    public static EV3ColorSensor colorSensor = null;


    public static void initComponents() {

        pushmotor   = new EV3MediumRegulatedMotor(MotorPort.A); // OK
        pressmotor  = new EV3LargeRegulatedMotor(MotorPort.B);
        ejectmotor  = new EV3LargeRegulatedMotor(MotorPort.C); //OK
        touchSensor = new EV3TouchSensor(SensorPort.S1);
        colorSensor= new EV3ColorSensor(SensorPort.S4);
        conveyor= new EV3LargeRegulatedMotor(MotorPort.D);
    }
}