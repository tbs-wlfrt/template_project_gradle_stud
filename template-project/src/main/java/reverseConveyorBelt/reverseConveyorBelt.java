package reverseConveyorBelt;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;


import java.util.Timer;
import java.util.TimerTask;


public class reverseConveyorBelt {

    static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.C);
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

    static EV3ColorSensor color =  new EV3ColorSensor(SensorPort.S4);



    public static int counter_g = 0;

    public static boolean reset_counter = false;


    public static TimerTask task = new TimerTask() {


        int counter = 0;

        public void run() {
            System.out.println("Task performed on: " + "n" +
                    "Thread's name: " + Thread.currentThread().getName());

            if (reset_counter){
                counter = -15;
                reset_counter = false;
            }

            counter++;
            counter_g = counter;
            System.out.println(counter);

        }
    };


    public static void main(String[] args) {


        Timer timer = new Timer("Timer");

        long delay = 1000L;

        timer.schedule(task, 0 ,delay);








     Random rand = new Random();



        double d1=0.0,d2=0.0,d3=0.0;
            int speed =200;
            motor.forward();

        int idcolor = 0;

            while (true) {

              //  LocalTime date = LocalTime.now();
              //  int seconds = date.toSecondOfDay();
               // System.out.println();

                idcolor = color.getColorID();
               // System.out.println("idcolor+"+idcolor);
                if (idcolor !=1){
                    reset_counter = true;
                }
                else
                    reset_counter = false;



                double ultra = counter_g; //(double)seconds%60 + (rand.nextDouble() % 10)+1 ;


                System.out.println("ULTRA" + ultra);

                // double ultra = 24;



                if (ultra>0 && ultra < 15.0) {
                    System.out.println("LOW " + ultra);

                    d1 = ultra / 15.0;
                    d2 = 0;
                    d3 = 0;

                    speed = 250;
                    System.out.println(d1 + " " + d2 + " " + d3);




                    double result_max = find_max(d1,d2,d3);

                    System.out.println("result-max"+result_max+"speed"+speed);

                    System.out.println ( (int)Math.round(speed*(result_max)) );
                    int result = ((int)Math.round(speed*(result_max)));

                    System.out.println("result"+result);

                    action(result);


                } else if (ultra >= 15.0 && ultra < 25.0) {

                    System.out.println("MEDIUM " + ultra);


                    d1 = (ultra - 15.0) / 15.0;
                    d2 = (40.0 - ultra) / 15.0;
                    d3 = 0;

                    speed = 350;
                    System.out.println(d1 + " " + d2 + " " + d3);


                    double result_max = find_max(d1,d2,d3);

                    System.out.println("result-max"+result_max+"speed"+speed);

                    System.out.println ( (int)Math.round(speed*(result_max)) );
                    int result = ((int)Math.round(speed*(result_max)));

                    System.out.println("result"+result);

                    action(result);


                } else if (ultra >= 25.0 && ultra <= 35.0) {

                    System.out.println("FAST " + ultra);


                    d1 = 0;
                    d2 = (35.0 - ultra) / 25.0;
                    d3 = (ultra - 25.0) / 25.0;


                   // speed = 200;
                    System.out.println(d1 + " " + d2 + " " + d3);
                    speed =600;

                    double result_max = find_max(d1,d2,d3);

                    System.out.println("result-max"+result_max+"speed"+speed);

                    System.out.println ( (int)Math.round(speed*(result_max)) );
                    int result = ((int)Math.round(speed*(result_max)));

                    System.out.println("result"+result);

                    action(result);



                }

               /* else if (ultra >= 36.0) {

                    System.out.println("FAST");


                    d1 = 0;
                    d2 = 0;
                    d3 = 1;


                    // speed = 200;
                    System.out.println(d1 + " " + d2 + " " + d3);
                    speed =600;

                    double result_max = find_max(d1,d2,d3);

                    System.out.println("result-max"+result_max+"speed"+speed);

                    System.out.println ( (int)Math.round(speed*(result_max)) );
                    int result = ((int)Math.round(speed*(result_max)));

                    System.out.println("result"+result);

                    action(result);



                }*/





            }








            }

            public static void action (int my_result){

                motor.rotate(my_result*-1);
                Delay.msDelay(10);

                motor.rotate(my_result+35);
                Delay.msDelay(10);

                motor.forward();


            }

    public static double find_max(double d1,double d2,double d3){

        double num1 = d1;
        double num2 = d2;
        double num3 = d3;
        double max;

        if (num1 >= num2 && num1 >= num3)
            max = num1;

        else if (num2 >= num1 && num2 >= num3)
            max = num2;
        else
            max = num3;

        return  max;

    }

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






}




