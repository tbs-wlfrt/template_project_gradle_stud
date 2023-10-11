package LineFollower;


import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MaximumFilter;
import lejos.utility.Delay;


//import jason.asSyntax.Literal;

public class LineFollower {


  //  static final EV3ColorSensor sensor = new EV3ColorSensor(SensorPort.S1); //drop sensor
  //  static  boolean call_mode =false;										//drop sensor


 //   static EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);     //sort Agent Color Sensor
 //   static  boolean call_mode2 =false;



  //  static EV3MediumRegulatedMotor motor1 = new EV3MediumRegulatedMotor(MotorPort.B); // Conveyor 2  Motor



  //  static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.D); // Conveyor 2  Motor


   // static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2); // DropButton

    //static EV3MediumRegulatedMotor dropmotor = new EV3MediumRegulatedMotor(MotorPort.A); // DropMotor




  //  static EV3LargeRegulatedMotor shredermotor = new EV3LargeRegulatedMotor(MotorPort.C); // ShredMotor
  //  static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S3);


    static EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A); //sag
    static EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B); //sol


    static final EV3ColorSensor color1 = new EV3ColorSensor(SensorPort.S3); //sol
    static EV3ColorSensor color2 = new EV3ColorSensor(SensorPort.S4); //sag



 //   public static boolean check_button() {


    //    boolean touch= touchSensor.isPressed();


   //     return touch;
        //if (distanceValue<100) {

  //  }

    public static float[] check_color (){



        SampleProvider spAmbient1 = color1.getRGBMode();
        SampleProvider spAmbient2 = color2.getRGBMode();

        SampleProvider max1 = new MaximumFilter(spAmbient1,5);
        SampleProvider max2 = new MaximumFilter(spAmbient2,5);

        float[] sample1 = new float[spAmbient1.sampleSize()];
        float[] sample2 = new float[spAmbient2.sampleSize()];

        max1.fetchSample(sample1, 0);  //sol
        max2.fetchSample(sample2, 0);  //sag



     //   System.out.println(sample1[0] + " " + sample2[0]);

        float[] vals = new float[2];
        vals[0] = sample1[0]; //sol
        vals[1] = sample2[0]; //sag


        return vals;
    }



    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorA.stop();
                motorB.stop();
            }
        }));



        float[] vals;

                // motor.stop();
                // motor.getPosition();

        //Calendar calendar = Calendar.getInstance();
        long previousTime = System.currentTimeMillis();

        int setPoint=22;  //182 OLD

        int lastError1=0;
        int lastError2=0;

        int cumError1=0;
        int cumError2=0;

        float Kp = 1.6F;   // 1.6 // 0.00001 //3.5
        float Ki= 0.00001F; //
        float Kd = 3.2F;


        System.out.println("previous time"+previousTime);


        while(true) {
            Delay.msDelay(20);
            vals = check_color ();
            System.out.println(vals[0] + " " + vals[1]);
            //creating Calendar instance

            //Returns current time in millis
            long currentTime = System.currentTimeMillis();;
            System.out.println("Time in milliseconds using Calendar: " + currentTime);

            long elapsedTime = (currentTime - previousTime);

            System.out.println("Elapsed Time "+elapsedTime);

            int error1 = (int) (setPoint - vals[0]);
            int error2 = (int) (setPoint - vals[1]);

            System.out.println("ERROR 1= "+ error1+" "+" ERROR 2= " + error2);

            cumError1 += (error1 * elapsedTime);
            cumError2 += (error2 * elapsedTime);

           int rateError1 = (int) ((error1 - lastError1)/elapsedTime);
           int rateError2 = (int) ((error2 - lastError2)/elapsedTime);

           int output1 = (int) (Kp * error1 + Ki * cumError1 + Kd * rateError1);
           int output2 = (int) (Kp * error2 + Ki * cumError2 + Kd * rateError2);

            lastError1 = error1;
            lastError2 = error2;
            previousTime = currentTime;
            System.out.println("****** output1 - MOTOR B- LEFT ="+output1+" "+" output2 - MOTOR A - RIGHT="+output2+"*************");

            if (output1 <0)
                output1=output1*-1;

            if (output2 <0)
                output2=output2*-1;


            motorA.setSpeed((int) (240-(output2)));  //out2
            motorB.setSpeed((int) (240-(output1)));  //out1
            Delay.msDelay(5);
         //   motorA.forward();
          //  motorB.forward();
            Delay.msDelay(5);




            /*if (value == true) {




            } else {

            //    motor1.stop();
                Delay.msDelay(1);

            }
/*


                // motor.stop();
                // motor.getPosition();


*/

            }



        }



        }
