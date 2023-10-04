package FuzzyTrial;

public class speed {

    public static void main(String[] args) {
        calculateAll(150.0);   //27 ile dene
        calculateAll(180.0);
        calculateAll(255.0);
        calculateAll(270.0);

        calculateAll(300.0);

        calculateAll(480.0);


        calculateAll(650.0);

        calculateAll(700.0);


        calculateAll(800.0);

        calculateAll(900.0);

    }

    public static void calculateAll(double asd){

        double n_cpuTemp = asd;

        if(n_cpuTemp<166.5)
        {
            //	System.out.println("1");
            double d1,d2,d3;

            d1=n_cpuTemp/166.5;
            d2=0.0;
            d3=0.0;


            System.out.println("1");


            System.out.println(d1+" "+d2+" "+d3);


        }else if(n_cpuTemp>=166.5 && n_cpuTemp<370.0 )
        {
            //	System.out.println("2");
            double d1,d2,d3;

            d1=(370.0-n_cpuTemp)/203.5;



            if (n_cpuTemp>=250.0) {

                d2=(n_cpuTemp-250.0)/249.5; // 10 ILE 19.5 arasi
            }
            else
                d2=0.0;

            d3=0.0;


            System.out.println("2");

            System.out.println(d1+" "+d2+" "+d3);



        }else if(n_cpuTemp>=370.0 && n_cpuTemp<499.5 )
        {
            //	System.out.println("3");
            double d1,d2,d3;
            d1=0;
            d2=(n_cpuTemp-250.0)/249.5;
            d3=	0;


            System.out.println("3");


            System.out.println(d1+" "+d2+" "+d3);


        }


        else if(n_cpuTemp>=499.5 && n_cpuTemp<750.0) {


            double d1,d2,d3;
            d1=0;
            d2=(750-n_cpuTemp)/250.5;

            if(n_cpuTemp>=666.0)
                d3=(n_cpuTemp-666.0)/166.5;
            else d3=0.0;

            System.out.println("4");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=750.0 && n_cpuTemp<=832.5) {


            double d1,d2,d3;
            d1=0;

           /* if (n_cpuTemp>21.0 && n_cpuTemp<26.0)  // 0 olabilir
            d2=(26-n_cpuTemp)/6.5;
            else */
            d2 =0.0;


            d3=	(n_cpuTemp-666.0)/166.5;


            System.out.println("5");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=832.5 && n_cpuTemp<=999.0) {


            double d1,d2,d3;
            d1=0;
            d2=0;
            d3 = (999.0-n_cpuTemp)/166.5;


            System.out.println("6");
            System.out.println(d1+" "+d2+" "+d3);


        }

    }
}
