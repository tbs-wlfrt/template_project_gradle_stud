package FuzzyTrial;

import java.util.Arrays;
import java.util.Collections;

public class trial_area {

    public static void main(String[] args) {


       // Selected:0.0 0.13333333333333333 0.5111331273948074
        scan_area(0.0, 0.13333333333333333, 0.5111331273948074);  //0.3,0.3,0.0

    }




    public static void scan_area(double bound1,double bound2,double bound3) {

        double bound_a=bound1;
        double bound_b=bound2;
        double bound_c=bound3;




        int low_b=decide_low_limit(bound_a,bound_b,bound_c);

        int high_b=decide_high_limit(bound_a,bound_b,bound_c);

        System.out.println(low_b+" "+high_b);

        double speed = 0.0;
        double d1 = 0.0, d2=0.0, d3=0.0;




        double ds1=0,ds2=0;
        double total1=0;
        double total2=0;




        for (speed  = high_b; speed <low_b; speed+=1) {



        if (speed < 166.5) {
            //	System.out.println("1");


            d1 = speed / 166.5;
            d2 = 0.0;
            d3=0.0;

        } else if (speed >= 166.5 && speed < 370.0) {
            //	System.out.println("2");


            d1 = (370.0 - speed) / 203.5;
            if (speed>=250.0) {

                d2=(speed-250.0)/249.5; // 10 ILE 19.5 arasi
            }
            else
                d2=0.0;

            d3=0.0;


        }

        else if(speed>=370.0 && speed<499.5 )
        {
            //	System.out.println("3");

            d1=0;
            d2=(speed-250.0)/249.5;
            d3=	0;

        }


        else if(speed>=499.5 && speed<750.0) {



            d1=0;
            d2=(750-speed)/250.5;
            d3=0.0;

           /*  if(n_cpuTemp>=666.0)
                d3=(n_cpuTemp-666.0)/166.5;
            else d3=0.0;
            */


        }
        else if(speed>=750.0 && speed<832.5) {


            d1=0;

           /* if (n_cpuTemp>21.0 && n_cpuTemp<26.0)  // 0 olabilir
            d2=(26-n_cpuTemp)/6.5;
            else */
            d2 =0.0;


            d3=	(speed-666.0)/166.5;





        }

        else if(speed>=832.5 && speed<=999.0) {



            d1=0;
            d2=0;
            d3 = (999.0-speed)/166.5;


         }







        double val = findMaxAndCut(d1,d2,d3,bound_a,bound_b,bound_c);
        total2+=val;

        total1 +=(val*speed);


        if (speed==368){

            System.out.println(d1+" "+d2);
        }



        }

        System.out.println(total1/total2);
    }




    private static int decide_high_limit(double bound1,double bound2,double bound3) {

        Integer[] low_bound ={5000,5000,5000};

        if (bound1 != 0.0){
            low_bound[0] =0;
        }

        if (bound2 != 0.0){
            low_bound[1] =250;
        }

        if (bound3 != 0.0){
            low_bound[2] =666;
        }


        return Collections.min(Arrays.asList(low_bound));


    }

    private static int decide_low_limit(double bound1,double bound2,double bound3) {

        Integer[] high_bound ={0,0,0};

        if (bound1 != 0.0){
            high_bound[0] =370;
        }

        if (bound2 != 0.0){
            high_bound[1] =750;
        }

        if (bound3 != 0.0){
            high_bound[2] =999;
        }

        return Collections.max(Arrays.asList(high_bound));


    }

    

    public static double findMaxAndCut(double d1, double d2,double d3,double bound1,double bound2,double bound3) {

        if (d1>bound1)
            d1=bound1;

        if(d2>bound2)
            d2=bound2;

        if(d3>bound3)
            d3=bound3;



        return Math.max(d3,Math.max(d1,d2));


    }


}