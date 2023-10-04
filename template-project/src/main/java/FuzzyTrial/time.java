package FuzzyTrial;

public class time {

    public static void main(String[] args) {

        calculateAll(9.0);



    }

    public static void calculateAll(double asd){

        double n_cpuTemp = asd;

        if(n_cpuTemp<10.0)
        {
            //	System.out.println("1");
            double d1,d2,d3;

            d1=n_cpuTemp/10.0;
            d2=0.0;
            d3=0.0;


            System.out.println("1");


            System.out.println(d1+" "+d2+" "+d3);


        }else if(n_cpuTemp>=10.0 && n_cpuTemp<25.0 )
        {
            //	System.out.println("2");
            double d1,d2,d3;

            d1=(25.0-n_cpuTemp)/15.0;



            if (n_cpuTemp>=15.0) {

                d2=(n_cpuTemp-15.0)/15.0; // 10 ILE 19.5 arasi
            }
            else
                d2=0.0;

            d3=0.0;


            System.out.println("2");

            System.out.println(d1+" "+d2+" "+d3);



        }else if(n_cpuTemp>=25.0 && n_cpuTemp<30.0 )
        {
            //	System.out.println("3");
            double d1,d2,d3;
            d1=0;
            d2=(n_cpuTemp-15.0)/15.0;
            d3=	0;


            System.out.println("3");


            System.out.println(d1+" "+d2+" "+d3);


        }


        else if(n_cpuTemp>=30.0 && n_cpuTemp<45.0) { // 30. 40


            double d1,d2,d3;
            d1=0;
            d2=(45.0-n_cpuTemp)/15.0;

            if(n_cpuTemp>=35.0)
                d3=(n_cpuTemp-35.0)/15.0;
            else d3=0.0;

            System.out.println("4");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=45.0 && n_cpuTemp<=50.0) {


            double d1,d2,d3;
            d1=0;
            d2=0;


            d3=	(n_cpuTemp-35.0)/15.0;


            System.out.println("5");

            System.out.println(d1+" "+d2+" "+d3);

        }

        else if(n_cpuTemp>=50.0 && n_cpuTemp<=60.0) {


            double d1,d2,d3;
            d1=0;
            d2=0;
            d3 = (50.0-n_cpuTemp)/10.0;


            System.out.println("6");

            System.out.println(d1+" "+d2+" "+d3);


        }

    }





}



