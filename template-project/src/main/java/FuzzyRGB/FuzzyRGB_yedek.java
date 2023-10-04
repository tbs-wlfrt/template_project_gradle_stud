package FuzzyRGB;


import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


//Jason Imports Start

//Jason Imports End


public class FuzzyRGB_yedek {



    public static void main(String[] args) {

        EV3ColorSensor color =  new EV3ColorSensor(SensorPort.S4);

        System.out.println("Switching to RGB Mode");
       // color.setFloodlight(true);
        SampleProvider sp = color.getRGBMode();

        int sampleSize = sp.sampleSize();
        float[] sample = new float[sampleSize];

        Delay.msDelay(2000);

        // Takes some samples and prints them

        System.out.println("*****START HERE*******");

        float red =0;
        float green =0;
        float blue =0;

            double sample_size = 1.0;

        for (int i = 1; i < 25; i++) {
            sp.fetchSample(sample, 0);


            	System.out.println("N=" + i + " Sample={}" + (sample[0]));
            	System.out.println("N=" + i + " Sample={}" + (sample[1]));
            	System.out.println("N=" + i + " Sample={}" + (sample[2]));

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


        double g1 = 0.0;
        double g2 = 0.0;
        double g3 = 0.0;


        double b1 = 0.0;
        double b2 = 0.0;
        double b3 = 0.0;

        int red =  0;
        int green = 0;
        int blue = 0;



         red = (int) r;
         green =(int) g;
         blue =(int) b;




        String s_red = "";
        String s_green = "";
        String s_blue = "";


        if (red < 15)    // low red
        {

            r1 = 1;
            r2 = 0;
            r3 = 0;
            System.out.println("low red" + " " + r1 + " " + r2 + " " + r3);

            s_red = "low red";

        } else if (red >= 15 && red < 35) {   // middle red

            r1 = (35.0 - red) / 20.0;
            r2 = (red - 15.0) / 20.0;
            r3 = 0;
            System.out.println("middle red" + " " + r1 + " " + r2 + " " + r3);

            s_red = "middle red";

        } else if (red >= 35 && red < 55) { // high red

            r1 = 0;
            r2 = (55.0 - red) / 20.0;
            r3 = (red - 35.0) / 20.0;
            System.out.println("high red" + " " + r1 + " " + r2 + " " + r3);


            s_red = "high red";

        } else if (red >= 55) { // very high red

            r1 = 0;
            r2 = 0;
            r3 = 1;
            System.out.println("very high red" + " " + r1 + " " + r2 + " " + r3);


            s_red = "very high red";

        }


// ---------------------------------- GREEN GREEN GREEN ---------------------------------------------

        if (green < 15)    // low green
        {

            g1 = 1;
            g2 = 0;
            g3 = 0;
            System.out.println("low green" + " " + g1 + " " + g2 + " " + g3);


            s_green = "low green";

        } else if (green >= 15 && green < 35) {   // middle green

            g1 = (35.0 - green) / 20.0;
            g2 = (green - 15.0) / 20.0;
            g3 = 0;
            System.out.println("middle green" + " " + g1 + " " + g2 + " " + g3);


            s_green = "middle green";

        } else if (green >= 35 && green < 55) { // high green

            g1 = 0;
            g2 = (55.0 - green) / 20.0;
            g3 = (green - 35.0) / 20.0;
            System.out.println("high green" + " " + g1 + " " + g2 + " " + g3);


            s_green = "high green";

        } else if (green >= 55) { // very high green

            g1 = 0;
            g2 = 0;
            g3 = 1;
            System.out.println("very high green" + " " + g1 + " " + g2 + " " + g3);


            s_green = "very high green";

        }


//--------------------------------   BLUE BLUE BLUE ---------------------------------------------


        if (blue < 15)    // low blue
        {

            b1 = 1;
            b2 = 0;
            b3 = 0;
            System.out.println("low blue" + " " + b1 + " " + b2 + " " + b3);


            s_blue = "low blue";

        } else if (blue >= 15 && blue < 35) {   // middle blue

            b1 = (35.0 - blue) / 20.0;
            b2 = (blue - 15.0) / 20.0;
            b3 = 0;
            System.out.println("middle blue" + " " + b1 + " " + b2 + " " + b3);

            s_blue = "middle blue";

        } else if (blue >= 35 && blue < 55) { // high blue

            b1 = 0;
            b2 = (55.0 - blue) / 20.0;
            b3 = (blue - 35.0) / 20.0;
            System.out.println("high blue" + " " + b1 + " " + b2 + " " + b3);


            s_blue = "high blue";

        } else if (blue >= 55) { // very high green

            b1 = 0;
            b2 = 0;
            b3 = 1;

            System.out.println("very high blue" + " " + b1 + " " + b2 + " " + b3);


            s_blue = "very high blue";

        }


        if (s_red.equals("middle red") && s_green.equals("low green") && s_blue.equals("middle blue")) {


            System.out.println("BROWN");


        }



        else if (s_red.equals("middle red") && s_green.equals("middle green") && s_blue.equals("very high blue")) {


            System.out.println("Koyu Mor");


        }

        else if (s_red.equals("very high red") && s_green.equals("high green") && s_blue.equals("low blue")) {


            System.out.println("Acik Sari 0");
        }



        else if (s_red.equals("very high red") && s_green.equals("high green") && s_blue.equals("middle blue")) {


            System.out.println("Acik Sari 1");


        } else if (s_red.equals("very high red") && s_green.equals("very high green") && s_blue.equals("middle blue")) {


            System.out.println("Acik Sari 2");  // Condition 2 for acik sari


        } else if (s_red.equals("very high red") && s_green.equals("very high green") && s_blue.equals("high blue")) {


            System.out.println("Acik Sari 3");  // Condition 3 for acik sari


        } else if (s_red.equals("very high red") && s_green.equals("high green") && s_blue.equals("high blue")) {


            System.out.println("Koyu Sari(Oralet)");


        } else if (s_red.equals("middle red") && s_green.equals("high green") && s_blue.equals("very high blue") ||
                (((r1 == 0) && (r2 >= 0.55 && r2 <= 1) && (g1 == 0) && (g2 >= 0.45 && g2 <= 0.9) && (g3 >= 0.1 && g3 <= 0.55)))) {


            System.out.println("Koyu Sari(Oralet)");


        }

        else if (s_red.equals("high red") && s_green.equals("middle green") && s_blue.equals("middle blue") ||
                (((r1 == 0) && (r2 >= 0.55 && r2 <= 1) && (g1 == 0) && (g2 >= 0.45 && g2 <= 0.9) && (g3 >= 0.1 && g3 <= 0.55)))) {


                System.out.println("Koyu Sari(Oralet) 2");


        }

        else if (s_red.equals("low red") && s_green.equals("middle green") && s_blue.equals("middle blue")) {



            System.out.println("Koyu Yesil*"); // Koyu yesilde possible


        }

        else if (s_red.equals("middle red") && s_green.equals("high green") && s_blue.equals("middle blue")) {


            System.out.println("Fistik Yesili");


        }

        else if (s_red.equals("high red") && s_green.equals("very high green") && s_blue.equals("middle blue")) {


            System.out.println("Fistik Yesili 2");


        }

        else if (s_red.equals("high red") && s_green.equals("very high green") && s_blue.equals("high blue")) {


            System.out.println("Fistik Yesili 3");


        }


        else if (s_red.equals("low red") && s_green.equals("middle green") && s_blue.equals("very high blue")) {


            System.out.println("Koyu Mavi");


        }

            else if (s_red.equals("low red") && s_green.equals("middle green") && s_blue.equals("high blue")) {


                System.out.println("Acik Mavi");


            }
            else if (s_red.equals("low red") && s_green.equals("middle green") && s_blue.equals("very high blue")) {


                System.out.println("Acik Mavi 2");

            }

             else if (s_red.equals("middle red") && s_green.equals("very high green") && s_blue.equals("very high blue")) {


                System.out.println("Acik Mavi 3");

        }



            else if (s_red.equals("low red") && s_green.equals("high green") && s_blue.equals("very high blue")) {


            System.out.println("TURKUAZ");


        }

        else if (s_red.equals("low red") && s_green.equals("low green") && s_blue.equals("middle blue")) {


            System.out.println("Koyu Yesil");


        } else if ((s_red.equals("high red") && s_green.equals("high green") && s_blue.equals("very high blue"))
                && (!((r1 == 0) && (r2 >= 0.55 && r2 <= 1) && (g1 == 0) && (g2 >= 0.45 && g2 <= 0.9) && (g3 >= 0.1 && g3 <= 0.55)))) {


            System.out.println("Lila");


        } else if (s_red.equals("very high red") && s_green.equals("very high green") && s_blue.equals("very high blue")) {


            System.out.println("Beyaz");


        } else if (s_red.equals("very high red") && s_green.equals("middle green") && s_blue.equals("high blue")) {


            System.out.println("Magenta");


        }

        else if (s_red.equals("high red") && s_green.equals("middle green") && s_blue.equals("high blue")) {


            System.out.println("Pembe");


        }



        else if (s_red.equals("middle red") && s_green.equals("middle green") && s_blue.equals("high blue")) {


            System.out.println("Mor");


        }

        else if (s_red.equals("middle red") && s_green.equals("middle green") && s_blue.equals("middle blue")) {


            System.out.println("Koyu Mor");


        }

        else if (s_red.equals("very high red") && s_green.equals("low green") && s_blue.equals("middle blue")) {


            System.out.println("Red");

        } else if (s_red.equals("high red") && s_green.equals("low green") && s_blue.equals("low blue")) {


            System.out.println("Delikli Red");

        } else if (s_red.equals("high red") && s_green.equals("middle green") && s_blue.equals("middle blue")) {


            System.out.println("Red");

        }else if (s_red.equals("very high red") && s_green.equals("middle green") && s_blue.equals("middle blue")) {


                System.out.println("Red");


        } else {

            System.out.println("null");
        }

        System.out.println("----------------------------------------------------------");

    }
}
