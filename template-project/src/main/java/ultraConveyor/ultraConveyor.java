package ultraConveyor;


import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import ev3dev.sensors.ev3.EV3TouchSensor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;

import java.util.Random;


//Jason Imports Start

import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Logger;
//Jason Imports End
import java.util.stream.IntStream;



public class ultraConveyor {

    static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.C);
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);


    public static void main(String[] args) {

        double d1=0.0,d2=0.0,d3=0.0;
        int speed =20;

        while (true) {

        double ultra = check_Emergency();

            System.out.println("ULTRA"+ultra);

           // double ultra = 24;

         //   if (ultra > 100){
           //     ultra = 100;

            if (ultra == 2147483647){
                    ultra =0;
            }
         //   }

        if(ultra<4)
        {
            System.out.println("LOW");

            d1=1;
            d2=0;
          //  d3=0;

            speed = 200;
            System.out.println(d1+" "+d2+" "+d3);
        }

        else if(ultra>=4 && ultra<60 ) {

            System.out.println("MEDIUM");


            d1 = (ultra-10)/100;
            d2 = (60 - ultra) / 100;
         //   d3 = (ultra-25)/25;

            speed = 400;
            System.out.println(d1+" "+d2+" "+d3);
        }

        else if(ultra>=60 && ultra<=110 )
        {

            System.out.println("HIGH");



            d1=(110-ultra)/50;
            d2=(ultra-60)/50;
         //   d3=1;



            speed = 450;
         //   System.out.println(d1+" "+d2+" "+d3);


        }

       else if(ultra>111 )
        {
            d1=0;
            d2=1;
            speed = 500;
        }



           // double result_max = find_max(d1,d2,d3);
            double result_max = Math.max(d1,d2);
            System.out.println("result-max"+result_max+"speed"+speed);

          int constant = 0;
            if (d1==1.0){
                constant = 1;
            }
            else{
                constant = 0;
            }



            System.out.println ( (int)Math.round(speed*(result_max*(1-constant))) );
            int result = ((int)Math.round(speed*(result_max*(1-constant))));

            System.out.println("result"+result);
            motor.setSpeed(result);
            Delay.msDelay(10);
            motor.forward();
        }







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
