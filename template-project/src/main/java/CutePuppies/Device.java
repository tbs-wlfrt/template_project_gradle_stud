package CutePuppies;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

import java.io.File;

public class Device {
    static EV3LargeRegulatedMotor motor_left = null;
    static EV3LargeRegulatedMotor motor_right = null;
    static EV3UltrasonicSensor sensor_sonic_front = null;
    static EV3UltrasonicSensor sensor_sonic_right = null;
    static EV3UltrasonicSensor sensor_sonic_left = null;



    public static void init() {
        System.out.println("Init starts");
       // Delay.msDelay(6000);
        motor_left = new EV3LargeRegulatedMotor(MotorPort.B);
        motor_right = new EV3LargeRegulatedMotor(MotorPort.C);
        sensor_sonic_front = new EV3UltrasonicSensor(SensorPort.S3);   // Front
        sensor_sonic_right = new EV3UltrasonicSensor(SensorPort.S4);   // Right
        sensor_sonic_left = new EV3UltrasonicSensor(SensorPort.S1);   // Left
        System.out.println("Init finishes");

        setSpeed(500);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Stop motors Ctrl+C");
                motor_left.stop();
                motor_right.stop();
            }
        }));

    }

    public static void fuzzy_speed_distance(double dist, double yaww){

        String filename = "";



        String os_type = System.getProperty("os.name");

        if (os_type.contains("Windows"))
            filename = "C:\\Users\\Burak\\OneDrive - Universiteit Antwerpen\\Images\\Hichem Boot\\7_9_21\\template_project_gradlecopie\\template-project\\src\\main\\java\\FuzzyTrial\\tipper.fcl";
        else
            filename = System.getProperty("user.dir") + File.separator + "tipper.fcl";

        FIS fis = FIS.load(filename, true);

        if (fis == null) {
            System.err.println("Can't load file: '" + filename + "'");
            System.exit(1);
        }

        // Get default function block
        FunctionBlock fb = fis.getFunctionBlock(null);


        fb.setVariable("obstacle_left", BadGirl.ultra_left);
        fb.setVariable("obstacle_right", BadGirl.ultra_right);
        fb.setVariable("obstacle_front", BadGirl.ultra_front);

        System.out.println(" Ultra Left: "+BadGirl.ultra_left+" Ultra Right: "+BadGirl.ultra_right);


        fb.setVariable("distance", dist);

        fb.evaluate();

        // Show output variable's chart
        fb.getVariable("speed").defuzzify();

        // Print ruleSet
        //	System.out.println(fb);
        double f_speed= fb.getVariable("speed").getValue();
        System.out.println("Speeds: " + f_speed) ;

        Device.setSpeed((int) Math.round(f_speed));





        double rot_angle = fb.getVariable("rotation").getValue();

        if (rot_angle < 180){
        rot_angle = rot_angle/2.0;}

        System.out.println("Rotation: " + rot_angle);





        if (rot_angle > 10.0 && rot_angle <= 178.0){

       //     double turn_angle = yaww+rot_angle;

            System.out.println("diff_angle SAG once"+rot_angle);

            double turn_angle = rot_angle;


            turn_angle = turn_angle % 360;
            while (turn_angle < 0) { //pretty sure this comparison is valid for doubles and floats
                turn_angle += 360.0;
            }


            System.out.println("diff_angle SAG snra"+turn_angle+"yaww:"+yaww);


            while (!(yaww>=(turn_angle-4.0) && yaww<=(turn_angle+4.0))){

                setSpeed(200);
               turnLeft();
             //  turnRight();
                yaww = Math.toDegrees(BadGirl.tag.yaw);
                System.out.println("Kurtarma Donusu SAG"+yaww);
            }
            forward();
            Delay.msDelay(2000);


        }

        else if (rot_angle < 357 && rot_angle > 180){

            System.out.println("diff_angle SOL once"+rot_angle);

      //      double target_diff = 360-rot_angle;
      //      double turn_angle = yaww-target_diff;
            double turn_angle = rot_angle;
            turn_angle = turn_angle % 360;
            while (turn_angle < 0) { //pretty sure this comparison is valid for doubles and floats
                turn_angle += 360.0;
            }

            System.out.println("diff_angle SOL snra"+turn_angle+"yaww:"+yaww);

            while (!(yaww>=(turn_angle-4.0) && yaww<=(turn_angle+4.0))){

                setSpeed(200);
                turnRight();
              //  turnLeft();
                yaww = Math.toDegrees(BadGirl.tag.yaw);
                System.out.println("Kurtarma Donusu SOL"+yaww);
            }
            forward();
            Delay.msDelay(2000);
        }





    }

    public static void stop() {
        motor_left.stop();
        motor_right.stop();
    }

    public static void backward() {
        motor_left.backward();
        motor_right.backward();
    }

    public static void forward() {
        motor_left.forward();
        motor_right.forward();
    }

    public static void turnLeft() {
        motor_left.stop();
        motor_right.forward();
    }

    public static void turnRight() {
        motor_left.forward();
        motor_right.stop();
    }

    public static void setSpeed(int speed) {
        motor_left.setSpeed(speed);
        motor_right.setSpeed(speed);
    }

    public static int lookAhead_front() {
        final SampleProvider sp = sensor_sonic_front.getDistanceMode();
        int distanceValue = 0;
        float [] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        distanceValue = (int)sample[0];

        if (distanceValue >150){
            distanceValue = 150;
        }

        return distanceValue;
    }

    public static int lookAhead_left() {
        final SampleProvider sp = sensor_sonic_left.getDistanceMode();
        int distanceValue = 0;
        float [] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        distanceValue = (int)sample[0];

        if (distanceValue >150){
            distanceValue = 150;
        }

        return distanceValue;
    }

    public static int lookAhead_right() {
        final SampleProvider sp = sensor_sonic_right.getDistanceMode();
        int distanceValue = 0;
        float [] sample = new float[sp.sampleSize()];
        sp.fetchSample(sample, 0);
        distanceValue = (int)sample[0];

        if (distanceValue >150){
            distanceValue = 150;
        }

        return distanceValue;
    }




}
