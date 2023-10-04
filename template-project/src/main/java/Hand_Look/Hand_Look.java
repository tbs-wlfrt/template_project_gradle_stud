package Hand_Look;


import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * The idea behind this script is that the robot "looks at your hand"
 * and reacts by moving forward or backward.
 *
 * We shall perform 3  core tasks in this file, which are:
 *
 * 1. We will program a scared robot.
 *    The scared robot backs away the nearer you to get to it.
 *    Look for "Scared Robot" in the code to get started on this.
 *
 * 2. We will program a friendly robot.
 *    The friendly robot comes closer the nearer you get to it.
 *    Look for "Friendly Robot" in the code to get started on this.
 *
 * 3. We will program a proportionally-friendly robot.
 *    The friendly robot comes closer the nearer you get to it.
 *    However, when coming closer, it adjusts its speed according to how close you are.
 *    Look for "Friendly Robot Proportional" in the code to get started on this.
 *
 * 4. EXTRA
 *    After completing all the previous tasks, you may try to flip task 3, so you program a proportionally-scared robot.
 *    Look for "Scared Robot Proportional" in the code to get started on this.
 *
 *    HAVE FUN!
 */
public class Hand_Look {

    /**
     * motor1 and motor2 are references to the motor ports on the robot,
     * through which we can control the robot.
     */
    static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.A); // Conveyor 2  Motor
    static EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.D); // Conveyor 2  Motor

    /**
     * ultrasonicSensor is a reference to the ultrasonic sensor on the robot,
     * from which we can obtain the distance between our "hand" and the robot.
     */
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);

    /**
     * This is the main function, where we will do most of the work.
     * It is what controls the robot.
     * It does this by getiing observations from the robot and deciding what to do. (VERY QUICKLY)
     *
     * To complete the tasks, scroll to the sections labelled TODO.
     *
     * To comment, add 2 forward slashes(//) at the beginning of the lines beneath the word TODO
     * that corresponds to the task you have finished.
     *
     * To uncomment, remove the 2 forward slashes(//) at the beginning of the lines beneath the word TODO
     * that corresponds to the task you have finished.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Run this portion of code forever.
        while (true) {
            // Store the distance observed by the robot.
            int observed_distance = checkDistance();

            // When the distance observed is below 60,
            // execute this portion of code.
            // 60 here serves as our proximity threshold.
            // At this distance, we consider our "hand"
            // to be near the robot.
            if (observed_distance < 60) {
                // TODO #1: Simply run this file to implement the scared robot.
                //
                //moveBackwardNoProportional(); // Scared Robot

                // TODO #2: Comment the previous line and uncomment the next line to implement the friendly robot.
                moveForwardNoProportional(); // Friendly Robot

                // TODO #3: Comment the previous line and uncomment the next line to implement
                //  the proportionally-friendly robot.
                //moveForwardProportional(observed_distance); // Friendly Robot Proportional

                // TODO #4: Comment the previous line and uncomment the next line to implement
                //  the proportionally-scared.
  //             moveBackwardProportional(observed_distance); // Scary Robot Proportional
            }
            // Otherwise, execute this portion of code.
            // (When the distance observed is 60 or above)
            else {
                stopMoving();
            }
        }

    }

    /**
     * This function checks the distance of your "hand" from the front of the sensors.
     *
     * We don't provide it any value, but it gives us a number in return.
     * This number is the distance it has measured using the sensor.
     * (int means it's a whole number)
     * @return int distanceValue
     */
    public static int checkDistance() {
        // Wait one microsecond, for any previous instruction to complete.
        Delay.msDelay(1);
        // Obtain a sample reading from the sensor.
        SampleProvider sp = ultrasonicSensor.getDistanceMode();
        float[] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        // Sample readings come as a collection, therefore we shall only take the first one.
        // One is enough because we are actually taking a reading every few seconds.
        int distanceValue = 0;
        distanceValue = (int) sample[0];
        // We print out the reading to the screen, so we can see what the robot is reading.
        System.out.println("Distance: " + distanceValue);
        // We return the value.
        return distanceValue;
    }

    /**
     * This function instructs the robot to move forward at a constant speed.
     *
     * It does this by setting the speed of the motors first and then setting their direction.
     *
     * It does not require any value from us, neither does it return us any value.
     * You may use it to complete TODO #2
     */
    public static void moveForwardNoProportional() {
        // Set the speed of the motors
        motor1.setSpeed(200);
        motor2.setSpeed(200);
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
        // Set the movement direction to forward.
        motor1.forward();
        motor2.forward();
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
    }

    /**
     * This function works just like moveForwardNoProportional(), except it instructs the robot to move backward.
     * You may use it to complete TODO #2
     */
    public static void moveBackwardNoProportional() {
        // Set the speed of the motors
        motor1.setSpeed(200);
        motor2.setSpeed(200);
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
        // Set the movement direction to backward.
        motor1.backward();
        motor2.backward();
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
    }

    /**
     * This function instructs the robot to move forward at a changing speed.
     *
     * It does this by setting the speed of the motors to a speed that is PROPORTIONAL to the
     * maximum speed of the robot.
     * Then it sets their direction just like the non-proportional variant of this function.
     *
     * It requires an observed_distance from us, but does not return any value.
     * You may use it to complete TODO #3
     *
     * @param observed_distance
     */
    public static void moveForwardProportional(int observed_distance) {
        // Calculate proportion of distance to apply
        int proportion = Math.abs(observed_distance/10);
        // Set the speed of the motors
        motor1.setSpeed(120*proportion);
        motor2.setSpeed(120*proportion);
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
        // Set the movement direction to forward.
        motor1.forward();
        motor2.forward();
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
    }

    /**
     * This function works just like moveForwardProportional(), except it instructs the robot to move backward.
     * You may use it to complete TODO #3
     *
     * @param observed_distance
     */
    public static void moveBackwardProportional(int observed_distance) {
        // Calculate proportion of distance to apply
        int proportion = Math.abs(observed_distance/10);
        // Set the speed of the motors
        motor1.setSpeed(120*proportion);
        motor2.setSpeed(120*proportion);
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
        // Set the movement direction to backward.
        motor1.backward();
        motor2.backward();
        // Set a delay to allow any previous instructions to complete.
        Delay.msDelay(1);
    }

    /**
     * This function instructs the robot to stop moving.
     *
     * It does this by calling the stop functions of each motor.
     *
     * We do not need to provide anything, and it does not give us any value in return.
     */
    public static void stopMoving(){
        motor1.stop();
        motor2.stop();
    }

}
