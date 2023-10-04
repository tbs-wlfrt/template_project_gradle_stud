package buttonExample;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3TouchSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

//import jason.asSyntax.Literal;

public class firstTest {

    //static final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1); //drop sensor
    //static boolean call_mode = false;                                        //drop sensor
    //static EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);     //sort Agent Color Sensor
    //static boolean call_mode2 = false;

    // todo: these should stay like they are
    static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.B); // Conveyor 2  Motor
    static EV3MediumRegulatedMotor motor2 = new EV3MediumRegulatedMotor(MotorPort.C); // Conveyor 2  Motor
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);

    static int sleepInterval = 5;

    //static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.D); // Conveyor 2  Moto
    //static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2); // DropButton
    // static EV3MediumRegulatedMotor dropmotor = new EV3MediumRegulatedMotor(MotorPort.A); // DropMotor
    // static EV3LargeRegulatedMotor shredermotor = new EV3LargeRegulatedMotor(MotorPort.C); // ShredMotor
    //static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);

//    public static boolean check_button() {
//        boolean touch = touchSensor.isPressed();
//        return touch;
//        //if (distanceValue<100) {
//    }
//
//    public static int ultra_sensor_based_motor() {
//        Delay.msDelay(magicNumber);
//        SampleProvider sp = ultrasonicSensor.getDistanceMode();
//        int distanceValue = 0;
//        float[] sample = new float[sp.sampleSize()];
//        sp.fetchSample(sample, 0);
//        distanceValue = (int) sample[0];
//        System.out.println("distance: " + distanceValue);
//        return distanceValue;
//    }

    public static void turnRightInPlace(int time){
        motor1.backward();
        Delay.msDelay(sleepInterval);
        motor2.forward();
        Delay.msDelay(sleepInterval);

        Delay.msDelay(time);
        Delay.msDelay(sleepInterval);

        motor1.stop();
        Delay.msDelay(sleepInterval);
        motor2.stop();
        Delay.msDelay(sleepInterval);
    }

    public static void turnLeftInPlace(int time){
        motor1.forward();
        Delay.msDelay(sleepInterval);
        motor2.backward();
        Delay.msDelay(sleepInterval);

        Delay.msDelay(time);
        Delay.msDelay(sleepInterval);

        motor1.stop();
        Delay.msDelay(sleepInterval);
        motor2.stop();
        Delay.msDelay(sleepInterval);
    }

    public static void startMoveForward(){
        motor1.backward();
        Delay.msDelay(sleepInterval);
        motor2.backward();
        Delay.msDelay(sleepInterval);
    }

    public static void stop(){
        motor1.stop();
        Delay.msDelay(sleepInterval);
        motor2.stop();
        Delay.msDelay(sleepInterval);
    }

    public static void startMoveBackwards(){
        motor1.forward();
        Delay.msDelay(sleepInterval);
        motor2.forward();
        Delay.msDelay(sleepInterval);
    }

    public static void moveForward(int time){
        motor1.backward();
        Delay.msDelay(sleepInterval);
        motor2.backward();
        Delay.msDelay(sleepInterval);

        Delay.msDelay(time);
        Delay.msDelay(sleepInterval);

        motor1.stop();
        Delay.msDelay(sleepInterval);
        motor2.stop();
        Delay.msDelay(sleepInterval);
    }

    public static void moveBackwards(int time){
        motor1.forward();
        Delay.msDelay(sleepInterval);
        motor2.forward();
        Delay.msDelay(sleepInterval);

        Delay.msDelay(time);
        Delay.msDelay(sleepInterval);

        motor1.stop();
        Delay.msDelay(sleepInterval);
        motor2.stop();
        Delay.msDelay(sleepInterval);
    }

    public static void pivotRightBy(int degrees, int speed){
        int curSpeed = motor2.getSpeed();
        motor2.setSpeed(speed);
        Delay.msDelay(sleepInterval);
        double angle = 1265.0/360.0*degrees;
        motor2.rotate((int) angle);
        Delay.msDelay(sleepInterval);
        motor2.setSpeed(curSpeed);
        Delay.msDelay(sleepInterval);
    }

    public static void pivotLeftBy(int degrees){
        double angle = 1265.0/360.0*degrees;
        motor1.rotate((int) angle);
        Delay.msDelay(sleepInterval);
    }

    public static void main(String[] args) {
        // todo: begin our code
        // setup
        SampleProvider sp = ultrasonicSensor.getDistanceMode();
        Delay.msDelay(1000); // init of the sensor
        float[] sample = new float[sp.sampleSize()];

        startMoveForward();

        // todo: make this nicer later on
        while(true){
            Delay.msDelay(10);

            sp.fetchSample(sample, 0);
            int distanceValue = (int) sample[0];
            System.out.println("Distance: " + distanceValue);

            if(5 <= distanceValue && distanceValue <= 25){
                // pivotRightBy(90, 600); // todo: this is a pita
                stop();
                // moveBackwards(2000);
                break;
            }
        }

//        motor1.setSpeed(300);
//        motor2.setSpeed(300);
//        Delay.msDelay(sleepInterval);
//        // moveForward(5000);
//        // motor2.rotate(360); // 90isch
//        pivotRightBy(360);
//        pivotLeftBy(360);
//        // motor2.rotate(1265/4); // 90isch
//        // Delay.msDelay(magicNumber);
//        //motor2.rotate(-1260);
//         // instructions
//         // moveForward(5000);
//         // moveBackwards(5000);
//         // turnLeftInPlace(1100); // 180
//        // turnLeftInPlace(550); // 180
//         // turnRightInPlace(5000);

        return;
        // todo: end our code
    }
}
