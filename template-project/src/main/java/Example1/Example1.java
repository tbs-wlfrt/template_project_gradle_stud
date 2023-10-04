package Example1;



import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import ev3dev.sensors.ev3.EV3TouchSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
//import jason.asSyntax.Literal;
import lejos.hardware.port.SensorPort;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;

import java.util.Random;






public class Example1 {


    static final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1); //drop sensor
    static  boolean call_mode =false;										//drop sensor


    static EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);     //sort Agent Color Sensor
    static  boolean call_mode2 =false;



    static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.B); // Conveyor 2  Motor
    static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.D); // Conveyor 2  Motor


    EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2); // DropButton

    static EV3MediumRegulatedMotor dropmotor = new EV3MediumRegulatedMotor(MotorPort.A); // DropMotor
    static EV3LargeRegulatedMotor shredermotor = new EV3LargeRegulatedMotor(MotorPort.C); // ShredMotor
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);


    public static int check_Emergency() {


        Delay.msDelay(1);
        SampleProvider sp = ultrasonicSensor.getDistanceMode();
        int distanceValue = 0;

        float[] sample = new float[sp.sampleSize()];

        sp.fetchSample(sample, 0);
        distanceValue = (int) sample[0];

        System.out.println("Distance: " + distanceValue);

        return distanceValue;


        //if (distanceValue<100) {

    }



    public static void main(String[] args) {


       while(true) {

            int value = check_Emergency();
           if (value>1000){
               value=1000;
           }

           motor.setSpeed(value*20);
           Delay.msDelay(1);
           motor.backward();
           Delay.msDelay(1);
           // motor.stop();
           // motor.getPosition();

       }

    }

}
