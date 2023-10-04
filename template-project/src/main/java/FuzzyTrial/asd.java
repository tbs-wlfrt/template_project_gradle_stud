package FuzzyTrial;

public class asd {


    public static void main(String[] args) {

     /*   calculateAll(18.0);
        calculateAll(19.0);
        calculateAll(23.0);*/
        calculateAll(30.66);   //27 ile dene





    }



    public static void calculateAll(double asd){

        double n_cpuTemp = asd;

        if(n_cpuTemp<6.5)
        {
            //	System.out.println("1");
            double d1,d2,d3;

            d1=n_cpuTemp/6.5;
            d2=0.0;
            d3=0.0;


            System.out.println("1");


            System.out.println(d1+" "+d2+" "+d3);


        }else if(n_cpuTemp>=6.5 && n_cpuTemp<18.0 )
        {
            //	System.out.println("2");
            double d1,d2,d3;

            d1=(18.0-n_cpuTemp)/11.5;



            if (n_cpuTemp>=10.0) {

                d2=(n_cpuTemp-10.0)/9.5; // 10 ILE 19.5 arasi
            }
            else
                d2=0.0;

            d3=0.0;


            System.out.println("2");

            System.out.println(d1+" "+d2+" "+d3);



        }else if(n_cpuTemp>=18.0 && n_cpuTemp<19.5 )
        {
            //	System.out.println("3");
            double d1,d2,d3;
            d1=0;
            d2=(n_cpuTemp-10.0)/9.5;
            d3=	0;


            System.out.println("3");


            System.out.println(d1+" "+d2+" "+d3);


        }


        else if(n_cpuTemp>=19.5 && n_cpuTemp<26.0) {


            double d1,d2,d3;
            d1=0;
            d2=(26-n_cpuTemp)/6.5;

            if(n_cpuTemp>=21)
            d3=(n_cpuTemp-21)/11.6;
            else d3=0.0;

            System.out.println("4");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=26.0 && n_cpuTemp<=32.5) {


            double d1,d2,d3;
            d1=0;

           /* if (n_cpuTemp>21.0 && n_cpuTemp<26.0)  // 0 olabilir
            d2=(26-n_cpuTemp)/6.5;
            else */
            d2 =0.0;


            d3=	(n_cpuTemp-21.0)/11.5;


            System.out.println("5");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=32.5 && n_cpuTemp<=39.0) {


            double d1,d2,d3;
            d1=0;
            d2=0;
            d3 = (39.0-n_cpuTemp)/6.5;


            System.out.println("6");
            System.out.println(d1+" "+d2+" "+d3);


    }

    }


}
