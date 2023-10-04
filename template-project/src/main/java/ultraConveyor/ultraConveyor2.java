package ultraConveyor;


import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


//Jason Imports Start

//Jason Imports End


public class ultraConveyor2 {

    static EV3LargeRegulatedMotor motor = new EV3LargeRegulatedMotor(MotorPort.C);
    static EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);


    public static void main(String[] args) {

        double d1=0.0,d2=0.0,d3=0.0;
        int speed =20;

        while (true) {

        double ultra = check_Emergency();

            System.out.println("ULTRA"+ultra);

           // double ultra = 24;

            if (ultra > 100){
                ultra = 100;
                if (ultra == 2147483647)
                ultra =0;
            }

        if(ultra<25)
        {
            System.out.println("LOW");

            d1=ultra / 25;
            d2=0;
            d3=0;

            speed = 100;
            System.out.println(d1+" "+d2+" "+d3);
        }

        else if(ultra>=25 && ultra<50 ) {

            System.out.println("MEDIUM");


            d1 = (ultra - 25) / 25;
            d2 = (50 - ultra) / 25;
            d3 = 0;

            speed = 200;
            System.out.println(d1+" "+d2+" "+d3);
        }

        else if(ultra>=50 && ultra<=100 )
        {

            System.out.println("FAST");



            d1=0;
            d2=(100-ultra)/50;
            d3=	(ultra-50)/50;



            speed = 300;
            System.out.println(d1+" "+d2+" "+d3);


        }



            double result_max = find_max(d1,d2,d3);

            System.out.println("result-max"+result_max+"speed"+speed);

            System.out.println ( (int)Math.round(speed*(result_max)) );
            int result = ((int)Math.round(speed*(result_max)));

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
