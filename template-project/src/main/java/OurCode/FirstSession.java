package OurCode;

import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class FirstSession {

    // TODO: Find out which of these is left and which of these is right and add the code here.
    // The declaration of the motors.
    static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.B);
    static EV3MediumRegulatedMotor motor2 = new EV3MediumRegulatedMotor(MotorPort.C);
    // The declaration of the ultrasonic sensor.
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S1);

    // Interval used to sleep between consecutive commands.
    static int sleepIntervalInMilliSeconds = 5;

    /**
     * This is needed to sync the java code with the slower mindstorms robot.
     * This needs to be called after each operation.
     */
    public static void sync(){
        Delay.msDelay(sleepIntervalInMilliSeconds);
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

    // The main event loop of the program.
    public static void main(String[] args) {
        // Setup the ultrasonicSensor (todo: there might be a better way to do this)
        SampleProvider sp = ultrasonicSensor.getDistanceMode();
        Delay.msDelay(1000); // Needed to wait for the sensor to initialise.
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

        // Test code, might be hady for later:
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
    }
}
