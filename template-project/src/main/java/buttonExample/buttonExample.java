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

public class buttonExample {


    static final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1); //drop sensor
    static boolean call_mode = false;                                        //drop sensor


    static EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);     //sort Agent Color Sensor
    static boolean call_mode2 = false;


    static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.B); // Conveyor 2  Motor
    static EV3MediumRegulatedMotor motor2 = new EV3MediumRegulatedMotor(MotorPort.C); // Conveyor 2  Motor


    //static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.D); // Conveyor 2  Motor


    static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2); // DropButton

    // static EV3MediumRegulatedMotor dropmotor = new EV3MediumRegulatedMotor(MotorPort.A); // DropMotor
    // static EV3LargeRegulatedMotor shredermotor = new EV3LargeRegulatedMotor(MotorPort.C); // ShredMotor
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);


    public static boolean check_button() {


        boolean touch = touchSensor.isPressed();


        return touch;
        //if (distanceValue<100) {

    }

    public static int ultra_sensor_based_motor() {
        Delay.msDelay(1);
        SampleProvider sp = ultrasonicSensor.getDistanceMode();
        int distanceValue = 0;
        float[] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        distanceValue = (int) sample[0];
        System.out.println("distance: " + distanceValue);
        return distanceValue;
    }

    public static void main(String[] args) {
        boolean pressed = true;
        while (true) {
            int distance = ultra_sensor_based_motor();
            if (distance > 50) {
                distance = 50;
            }
            int speed = (Math.abs(distance - 20)) * 20;
            System.out.println("speed: " + speed);

            if (check_button()) {
                pressed = !pressed;
            }
            if (pressed) {
                motor1.setSpeed(0);
                motor2.setSpeed(0);
//                Delay.msDelay(1);
                motor1.forward();
                motor2.forward();
                //Delay.msDelay(1);
            } else {


                if (distance < 15) {
                    motor1.setSpeed(speed);
                    motor2.setSpeed(speed);
                    //Delay.msDelay(1);
                    motor1.forward();
                    motor2.backward();
                    //Delay.msDelay(1);
                } else if (distance > 25) {
                    motor1.setSpeed(speed);
                    motor2.setSpeed(speed);
                    //Delay.msDelay(1);
                    motor1.forward();
                    motor2.forward();
                    //Delay.msDelay(1);
                } else {
                    motor1.setSpeed(0);
                    motor2.setSpeed(0);
                    //Delay.msDelay(1);
                    motor1.forward();
                    motor2.forward();
                    //Delay.msDelay(1);
                }

                //motor1.setSpeed(distance*10);
                //motor2.setSpeed(distance*10);
                //motor1.backward();
                //motor2.backward();
                // Delay.msDelay(1);
            }

/*


                // motor.stop();
                // motor.getPosition();




            }



        }

*/

        }
    }
}
