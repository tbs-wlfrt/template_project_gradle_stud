package SingleLineFollower;


import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MaximumFilter;
import lejos.utility.Delay;

/**
 * In this example, we are going to tune the PID controller to have the robot follow the black line.
 *
 * */


public class SingleLineFollower {


    /** These are the motor and color sensor configurations that attached to the hardware **/

    static EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.A);
    static EV3LargeRegulatedMotor motorC = new EV3LargeRegulatedMotor(MotorPort.D);
    static final EV3ColorSensor color1 = new EV3ColorSensor(SensorPort.S4);


    /** This function samples the color values through the Color Sensor we configured,
     *  for stability it samples 5 color values and takes the average */
    public static float[] check_color() {


        SampleProvider spAmbient1 = color1.getRGBMode();
        SampleProvider max1 = new MaximumFilter(spAmbient1, 5);
        float[] sample1 = new float[spAmbient1.sampleSize()];
        max1.fetchSample(sample1, 0);
        float[] vals = new float[2];
        vals[0] = sample1[0];
        return vals;
    }



    /** This is an emergency function,
     *  when the application is halted, the motors are stopped**/
    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorB.stop();
                motorC.stop();
            }
        }));

/** The variables for the PID controller are defined here **/

        float[] vals;
        long previousTime = System.currentTimeMillis();

        /**This is the set point that our robot will track **/
        int setPoint = 76;

        int lastError1 = 0;
        int lastError2 = 0;

        int cumError1 = 0;
        int cumError2 = 0;
        /**        We should not modify the above values  **/


        /**        We are going to modify the below parameters: Kp, Ki, Kd **/


        float Kp = 5.30F;
        float Ki = 0.00001F;
        float Kd = 500.2F;


        /**        We are going to modify the below parameters: Kp, Ki, Kd **/



        /** The calculation for the black line tracking is done below.
         * It should not be modified **/

        while (true) {
            Delay.msDelay(20);
            vals = check_color();


            //We get the current time of the system
            long currentTime = System.currentTimeMillis();



            //We calculate the elapsed time.
            long elapsedTime = (currentTime - previousTime);

            System.out.println("Elapsed Time " + elapsedTime);

            int error1 = 0;
            int error2 = 0;


//To define the error margin we should calculate the deviation from the set point.


            if (vals[0] > 20 && vals[0] <= 75) {
                error1 = (int) (setPoint - (vals[0]));
            }


            if (vals[0] > 75 && vals[0] <= 156) {
                error2 = (int) (setPoint - vals[0]);
            }



            cumError1 += (error1 * elapsedTime);
            cumError2 += (error2 * elapsedTime);

            int rateError1 = (int) ((error1 - lastError1) / elapsedTime);
            int rateError2 = (int) ((error2 - lastError2) / elapsedTime);


            int output1 = (int) (Kp * error1 + Ki * cumError1 + Kd * rateError1);
            int output2 = (int) (Kp * error2 + Ki * cumError2 + Kd * rateError2);


            lastError1 = error1;
            lastError2 = error2;
            previousTime = currentTime;




            if (output1 <0)
                output1=output1*-1;

            if (output2 <0)
                output2=output2*-1;

/** Once we get the error results for deviating left or right the corresponding motors' speed should be changed.
 If one motor has more speed than the other, then we can create a turn for left or right **/

            motorB.setSpeed((int) (240 - (output2)));  //out2
            motorC.setSpeed((int) (240 - (output1)));  //out1
            Delay.msDelay(5);

            motorB.forward();
            motorC.forward();
            Delay.msDelay(5);


/** The calculation instructions of the PID for the black line tracking ends here.
 * Above values should not be modified **/


        }


    }


}

