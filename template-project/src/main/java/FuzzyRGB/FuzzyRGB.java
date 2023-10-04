package FuzzyRGB;



import Layer1_Dev.InitComp;
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


import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

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




public class FuzzyRGB {

    static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.B);

    static EV3LargeRegulatedMotor motor2 =  new EV3LargeRegulatedMotor(MotorPort.D);

    static EV3MediumRegulatedMotor pushmotor = new EV3MediumRegulatedMotor(MotorPort.A);

    static EV3ColorSensor color =  new EV3ColorSensor(SensorPort.S4);

    static float[] sample ;
    public static void main(String[] args) {



        System.out.println("Switching to RGB Mode");
       // color.setFloodlight(true);
        SampleProvider sp = color.getRGBMode();

        int sampleSize = sp.sampleSize();

        sample  = new float[sampleSize];

        int idcolor = 0;

        Delay.msDelay(2000);

        motor.setSpeed(300);
        motor.forward();

        // Takes some samples and prints them

        System.out.println("*****START HERE*******");

        float red =0;
        float green =0;
        float blue =0;

            double sample_size = 5.0;


        for (int i = 1; i < 5000; i++) {

          sp.fetchSample(sample, 0);
            //    idcolor = color.getColorID();
            	System.out.println("N=" + i + " Sample={}" + (sample[0]));
            	System.out.println("N=" + i + " Sample={}" + (sample[1]));
            	System.out.println("N=" + i + " Sample={}" + (sample[2]));

            if (sample[0]> 17.0 || sample[1]> 17.0  || sample[2] > 17.0){
                motor.stop();
            }



                System.out.println("COLOR:"+idcolor);

                red +=sample[0];
                green +=sample[1];
                blue +=sample[2];

            if (i % (int)sample_size ==0)
            {
                System.out.println("countta");
                red /=sample_size;
                green /=sample_size;
                blue /=sample_size;


                fuzzyColorSensor(red,green,blue);

                red = 0;
                green  = 0;
                blue = 0;
            }



            //Color cl = new Color((int)(sample[0] * 255), (int)(sample[1] * 255), (int)(sample[2] * 255));
            //	System.out.println(cl.getRed() +" " +cl.getGreen() + " "+ cl.getBlue() + " " + cl.getColor());
               Delay.msDelay(100);
        }

        sp = color.getAmbientMode();
        sampleSize = sp.sampleSize();
        sample = new float[sampleSize];
        sp.fetchSample(sample, 0);
        System.out.println("Ambient" + (sample[0]));



    }

    public static void fuzzyColorSensor(float r, float g, float b) {

        double r1 = 0.0;
        double r2 = 0.0;
        double r3 = 0.0;
        double r4 = 0.0;
        double r5 = 0.0;


        double g1 = 0.0;
        double g2 = 0.0;
        double g3 = 0.0;
        double g4 = 0.0;
        double g5 = 0.0;


        double b1 = 0.0;
        double b2 = 0.0;
        double b3 = 0.0;
        double b4 = 0.0;
        double b5 = 0.0;

        int red =  0;
        int green = 0;
        int blue = 0;



         red = (int) r;
         green =(int) g;
         blue =(int) b;




        String s_red = "";
        String s_green = "";
        String s_blue = "";


        String result = "";


        if (red < 25)    // low red
        {

            r1 = (25.0-red)/25.0;
            r2 = red/25.0;
            r3 = 0;
        //    System.out.println("low red" + " " + r1 + " " + r2 + " " + r3);

            s_red = "low red";

        } else if (red >= 25 && red < 50) {   // middle red

            r1 = 0;
            r2 = (50.0 - red) / 25.0;
            r3 = (red-25.0)/25.0;
            //   System.out.println("middle red" + " " + r1 + " " + r2 + " " + r3);

            s_red = "middle red";

        } else {


            r1=0;
            r2=0;
            r3=1;
        }



        if (blue < 25)    // low red
        {

            b1 = (25.0-blue)/25.0;
            b2 = blue/25.0;
            b3 = 0;
            //     System.out.println("low blue" + " " + b1 + " " + b2 + " " + b3);

            s_red = "low blue";

        } else if (blue >= 25 && blue < 50) {   // middle red

            b1 = 0;
            b2 = (50.0 - blue) / 25.0;
            b3 = (blue-25.0)/25.0;
            //      System.out.println("middle blue" + " " + b1 + " " + b2 + " " + b3);

            s_blue = "middle blue";

        } else {


            b1=0;
            b2=0;
            b3=1;
        }


        if (green < 32.5)    // low red
        {

            g1 = (32.5-green)/32.5;
            g2 = green/32.5;
            g3 = 0;
            //     System.out.println("low green" + " " + r1 + " " + r2 + " " + r3);

            s_green = "low green";

        } else if (green >= 32.5 && green < 65) {   // middle red

            g1 = 0;
            g2 = (65.0 - green) / 32.5;
            g3 = (green-32.5)/32.5;
            //    System.out.println("middle green" + " " + r1 + " " + r2 + " " + r3);

            s_green = "middle green";

        } else {


            g1=0;
            g2=0;
            g3=1;
        }


        System.out.println("Red: Low="+r1+" "+"Middle"+r2+"High"+r3);
        System.out.println("Green: Low="+g1+" "+"Middle"+g2+"High"+g3);
        System.out.println("Blue: Low="+b1+" "+"Middle"+b2+"High"+b3);


    if (!(s_red.equals("low red") && s_green.equals("low green") && s_blue.equals("low blue"))){



        if(r3>r2 ){
            System.out.println("RED HIGH");
            if ( (b1>b2) && (g1>g2)) {

                if ((g1 >= 0.5 && g1 <= 0.65) && (b1 >= 0.52 && b1 <= 0.93)) {
                    GoRedPosition(); push();
                    System.out.println("RED"+red);
                }

                  else if ((g1 > 0.5 && g1 <= 0.6) && (b1 <= 0.65 && b1 >= 0.6)){


                    System.out.println("RED"+red);
                    GoSpoiledRedPosition();
                    push();

                }




                System.out.println("Blue Low - Green Low");

            }else if (b1>b2 && g2>g1){

                System.out.println("Blue Low - Green Medium");

                if ((g2 > 0.51 && g2 <= 0.8) && (b1 >= 0.65 && b1 <= 0.84)) {
                    GoRedPosition(); push();
                }

                else if ((g2 > 0.5 && g2 <= 0.99) && (b1 >= 0.55 && b1 < 0.65)){

                    GoSpoiledRedPosition();
                    push();

                }

            }

            else if (b2>b2 && g2>g1){

                System.out.println("Blue Medium - Green Medium");

                if ((g2 > 0.7 && g2 <= 0.87) && (b2 >= 0.55 && b2 < 0.65)){

                    GoSpoiledRedPosition();
                    push();

                }

            }



            else{
                System.out.println("ELSE 1");
            }
        }






        else if(r2>r1 ){

            System.out.println("RED MEDIUM");
            if ( (b1>b2) && (g1>g2)) {

                System.out.println("Blue Low - Green Low");

                if ((g1 > 0.55 && g1 <= 0.65) && (b1 <= 0.65 && b1 >= 0.55)) {
                    GoSpoiledRedPosition();
                    push();
                    System.out.println("RED"+red);
                }

                else if ((g1 > 0.53 && g1 <= 0.88) && (b1 >= 0.55 && b1 <= 0.88)) {
                    GoRedPosition(); push();
                    System.out.println("RED"+red);
                }


            }
            if ( (b1>b2) && (g2>g1)) {

                if ((g2 > 0.55 && g2 <= 0.6) && (b1 >= 0.7 && b1 <= 0.8)) {
                    GoSpoiledRedPosition();
                    push();
                }

                System.out.println("Blue Low - Green Medium");
                GoRedPosition(); push();
            }

            if ( (b2>b1) && (g1>g2)) {


                    GoSpoiledRedPosition();
                    push();


                System.out.println("Blue Medium - Green Low");
                GoRedPosition(); push();
            }



            else{
                System.out.println("ELSE 2");
               // GoSpoiledRedPosition();
                //push();

            }

            }


    }



        if (!(sample[0]> 17.0 || sample[1]> 17.0  || sample[2] > 17.0)){
            motor.forward();
        }



/*
        if (result.equals("NormalGreen") || result.equals("DarkGreen") || result.equals("LightGreen")){
            GoGreenPosition();
            push();
        }
*/



        FileWriter fw = null;
        try {
            fw = new FileWriter("/home/robot/productionLineFuzzy/src/java/fuzzy.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // BufferedWriter bw = new BufferedWriter(fw);
        //   bw.write(motor1.getSpeed());
        //   bw.newLine();
        //  bw.close();


        try {
            fw.write(sample[0]+","+sample[1]+","+sample[2]);
            fw.write(System.getProperty( "line.separator" ));

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


     /*   if (result.equals("Red"))
            GoRedPosition(); push();
        else if (result.equals("SpoiledRed")){
            GoSpoiledRedPosition();
            push();
        }*/

        System.out.println("----------------------------------------------------------");

    }

    public  static void push(){
            Delay.msDelay(1000);
            pushmotor.setSpeed(800);
            pushmotor.forward();
            Delay.msDelay(500);
            pushmotor.stop();
            Delay.msDelay(800);
            pushmotor.backward();
            Delay.msDelay(500);
            pushmotor.stop();


    }

    public static void GoRedPosition() {
        motor2.setSpeed(800);
        Delay.msDelay(1);
        motor2.backward();
        Delay.msDelay(2500);
        motor2.stop();
        motor2.getPosition();

        //motor.setSpeed(150);
        //motor.forward();
    }

    public static void GoGreenPosition() {
        motor2.setSpeed(800);
        Delay.msDelay(1);
        motor2.backward();
        Delay.msDelay(1050);
        motor2.stop();
        motor2.getPosition();
    }


    public static void GoSpoiledRedPosition() {
        motor2.setSpeed(800);
        Delay.msDelay(1);
        motor2.backward();
        Delay.msDelay(825);
        motor2.stop();
        motor2.getPosition();

      //  motor.setSpeed(150);
      //  motor.forward();
    }







}
