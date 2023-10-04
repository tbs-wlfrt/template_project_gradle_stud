package Layer2_Dev;

import lejos.utility.Delay;

public class PressMotor {

    public static void pressDown() {

        //if (dev2.ispressed()) {
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(1);
              InitComp2.pressmotor.backward();
              Delay.msDelay(5000);
              InitComp2.pressmotor.stop();
           }

    public static void pressDown1() {
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(10);
        InitComp2.pressmotor.backward();
        Delay.msDelay(4200);
        InitComp2.pressmotor.stop();
    }

    public static void pressUp() {
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(1);
        while (true) {
            InitComp2.pressmotor.forward();
            if (dev2.ispressed()) {
                Delay.msDelay(100);
                InitComp2.pressmotor.stop();
                break;
            }
        }

    }
    public static void pressUp1() {
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(1);
        while (true) {
            InitComp2.pressmotor.forward();
            if (dev2.ispressed()) {
                Delay.msDelay(100);
                InitComp2.pressmotor.stop();
                break;
            }
       }
    }
    public static void mediumpress() {
        /***medium press to fixe the brick  */
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(1);
        InitComp2.pressmotor.forward();
        Delay.msDelay(2000);
        InitComp2.pressmotor.stop();
        Delay.msDelay(300);
        InitComp2.pressmotor.backward();
        Delay.msDelay(2000);
        InitComp2.pressmotor.stop();

    }

    public static void resetposition() {  /***get the initial position */
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(1);
        while (true) {
          if (!dev2.ispressed()){
            InitComp2.pressmotor.forward();
        }
        else if(dev2.ispressed()) {
            InitComp2.pressmotor.stop();
            Delay.msDelay(1000);
            InitComp2.pressmotor.backward();
            Delay.msDelay(5000);
            InitComp2.pressmotor.stop();

            break;
        }
        }
    }












/*public static void pressDown() {
    InitComp2.pressmotor.resetTachoCount();
    InitComp2.pressmotor.setSpeed(60);
    Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*
    InitComp2.pressmotor.rotate(-150);
    System.out.println(InitComp2.pressmotor.getPosition());

}
    public static void pressDown1() {
        InitComp2.pressmotor.resetTachoCount();
        InitComp2.pressmotor.setSpeed(60);
        Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*

        InitComp2.pressmotor.rotateTo(-300);
        System.out.println(InitComp2.pressmotor.getPosition());

    }
    public static void pressUp() {
        InitComp2.pressmotor.resetTachoCount();
        InitComp2.pressmotor.setSpeed(70);
        Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*

        InitComp2.pressmotor.rotateTo(0);
        System.out.println(InitComp2.pressmotor.getPosition());

    }
    public static void pressUp1() {
        InitComp2.pressmotor.resetTachoCount();
        InitComp2.pressmotor.setSpeed(70);
        Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*

        InitComp2.pressmotor.rotateTo(30);
    }
    public static void pressSmDown() {
        InitComp2.pressmotor.resetTachoCount();
        InitComp2.pressmotor.setSpeed(70);
        Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*

        InitComp2.pressmotor.rotateTo(-150);
        System.out.println(InitComp2.pressmotor.getPosition());

    }
    public static void pressSmUp() {
       // InitComp2.pressmotor.resetTachoCount();
        InitComp2.pressmotor.setSpeed(60);
       // Delay.msDelay(10);
    *//*InitComp2.pressmotor.brake();
    Delay.msDelay(10);*//*
        Delay.msDelay(1);
        InitComp2.pressmotor.rotateTo(-100);
        System.out.println(InitComp2.pressmotor.getPosition());

    }*/

   /* public static void pressDown(int count) {
          if (count==1){
             runDegs(360,40);
              }
        else {
        runDegs(300,40);
    }

    }
    public static void pressUp(int count) {
        if (count==1){
            runDegs(-300,40);
        }
        else {
            runDegs(-270,40);
        }

    }

    public static void pressSmDown(){
        runDegs(70,40);
    }
    public static void pressSmUp(){
        runDegs(-50,40);
    }
    public static void runDegs(int angle, int speed){
        InitComp2.pressmotor.setSpeed(speed);
        Delay.msDelay(1);
        InitComp2.pressmotor.rotate(angle);
    }*/
}
