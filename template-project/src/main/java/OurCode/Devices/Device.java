package OurCode.Devices;


import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MaximumFilter;
import lejos.utility.Delay;

public class Device {
    //Motors instantiation
    static EV3MediumRegulatedMotor motor1;
    static EV3MediumRegulatedMotor motor2;

    // The declaration of the ultrasonic sensor.
    static EV3UltrasonicSensor ultrasonicSensor;
    static EV3ColorSensor frontColorSensor;
    static EV3ColorSensor backColorSensor;


    static int sleepIntervalInMilliSeconds = 5;

    //sample providers
    static SampleProvider ultrasonicSP;
    static SampleProvider frontColorSP;
    static SampleProvider frontColorMax;
    static SampleProvider backColorSP;





    public static void init() {
        System.out.println("Initialising...");
        //assigning motors
        motor1 = new EV3MediumRegulatedMotor(MotorPort.B); // left
        motor2 = new EV3MediumRegulatedMotor(MotorPort.C); // right

        ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
        frontColorSensor = new EV3ColorSensor(SensorPort.S1);
        backColorSensor = new EV3ColorSensor(SensorPort.S2);


        ultrasonicSP = ultrasonicSensor.getDistanceMode();

        frontColorSP = frontColorSensor.getRGBMode();
        frontColorMax = new MaximumFilter(frontColorSP, 5);

        backColorSP = backColorSensor.getColorIDMode();


        Delay.msDelay(1000); // Wait for the sensors to initialise.

        System.out.println("Finished initialisation.");
    }


    // TODO: Move these out into another file maybe?
    /**
     * This is needed to sync the java code with the slower mindstorms robot.
     * This needs to be called after each operation.
     */
    public static void sync(){
        Delay.msDelay(sleepIntervalInMilliSeconds);
    }
    public static void sync(int multiplier){
        Delay.msDelay((long) multiplier *sleepIntervalInMilliSeconds);
    }
    /**
     * Turn the robot clockwise around the center-point of rotation for *time* duration.
     * @param time The duration in ms to turn for.
     */
    public static void turnRightInPlace(int time){
        motor1.backward(); sync();
        motor2.forward(); sync();

        Delay.msDelay(time); sync();

        motor1.stop(); sync();
        motor2.stop(); sync();
    }

    /**
     * Turn the robot counter-clockwise around the center-point of rotation for *time* duration.
     * @param time The duration in ms to turn for.
     */
    public static void turnLeftInPlace(int time){
        motor1.forward(); sync();
        motor2.backward(); sync();

        Delay.msDelay(time); sync();

        motor1.stop(); sync();
        motor2.stop(); sync();
    }

    /**
     * Start to move the robot forward indefinitely.
     */
    public static void startMoveForward(){
        motor1.backward(); sync();
        motor2.backward(); sync();
    }

    /**
     * Stop the robot.
     */
    public static void stop(){
        motor1.stop(); sync();
        motor2.stop(); sync();
    }

    /**
     * Move the robot backwards indefinitely.
     */
    public static void startMoveBackwards(){
        motor1.forward(); sync();
        motor2.forward(); sync();
    }

    /**
     * Move the robot forward for *time* duration.
     * @param time The duration in ms to move for.
     */
    public static void moveForward(int time){
        motor1.backward(); sync();
        motor2.backward(); sync();

        Delay.msDelay(time); sync();

        motor1.stop(); sync();
        motor2.stop(); sync();
    }

    /**
     * Move the robot backwards for *time* duration.
     * @param time The duration in ms to move for.
     */
    public static void moveBackwards(int time){
        motor1.forward(); sync();
        motor2.forward(); sync();

        Delay.msDelay(time); sync();

        motor1.stop(); sync();
        motor2.stop(); sync();
    }

    /**
     * Pivot the robot clockwise around the right wheel *degrees* with *speed*.
     * @param degrees The degrees to pivot for, with 360 being a full pivot.
     * @param speed The speed of the motor when pivoting.
     */
    public static void pivotRightBy(int degrees, int speed){
        int curSpeed = motor2.getSpeed();
        double angle = 1265.0/360.0*degrees;

        motor2.setSpeed(speed); sync();
        motor2.rotate((int) angle); sync();
        motor2.setSpeed(curSpeed); sync();
    }

    /**
     * Pivot the robot counter-clockwise around the right wheel *degrees* with *speed*.
     * @param degrees The degrees to pivot for, with 360 being a full pivot.
     * @param speed The speed of the motor when pivoting.
     */
    public static void pivotLeftBy(int degrees, int speed){
        int curSpeed = motor1.getSpeed();
        double angle = 1265.0/360.0*degrees;

        motor1.setSpeed(speed); sync();
        motor1.rotate((int) angle); sync();
        motor1.setSpeed(curSpeed); sync();
    }

    /**
     * Sample the front ultrasonic sensor and return distance reading.
     * @return current distance reading from the sensor.
     */
    public static int sampleFrontDistance(){
        float[] sample = new float[ultrasonicSP.sampleSize()];
        ultrasonicSP.fetchSample(sample, 0);
        return (int) sample[0];
    }


    /**
     * Sample the light sensor and return intensity reading.
     * @return current light intensity reading from the sensor.
     */
    public static float sampleLightIntensity(){
        float[] sample = new float[frontColorSP.sampleSize()];
        frontColorMax.fetchSample(sample, 0);

        //float[] vals = new float[2];
        return sample[0];
    }


    /**
     * Sample the back color sensor and returns the ID of the current color.
     * @return current color ID reading from the sensor.
     */
    public static int sampleBackColor(){
        float[] backSample = new float[backColorSP.sampleSize()];
        backColorSP.fetchSample(backSample, 0);
        /*
        float[] vals = new float[2];
        vals[0] = frontSample[0];
        vals[1] = backSample[0];
         */
        return (int) backSample[0];
    }

    /**
     * Sample the light sensor and return intensity reading.
     * @param speed1 speed for motor 1.
     * @param speed2 speed for motor 2.
     */
    public static void setMotorSpeeds(int speed1, int speed2){
        motor1.setSpeed(speed1); sync();
        motor2.setSpeed(speed2); sync();

    }


}
