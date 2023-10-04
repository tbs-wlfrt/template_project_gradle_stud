package Layer1_Dev;

import java.rmi.RemoteException;

public class dev1 {

    public static boolean ispressed() {

    return  Button.isPressed();

    }
    public static void idcolor() {
     DropSensor.isProductReady();
    }
    public static boolean readcolor() {
        return DropSensor.isProductReady();

    }
    //********** ConveyorMotor 1 *********
    public static void startconvoyor1(){

            ConveyorMotor.runconveyor1(800);
    }
    public static void re_startconvoyor1(){

        ConveyorMotor.re_runconveyor1(500);
    }
    public static boolean Stuck() throws RemoteException {
        return ConveyorMotor.isStalled();
    }
    public static void SolveStucking() {
        ConveyorMotor.solvestucking();
    }
    public static void startslaowconvoyor1() {
        ConveyorMotor.runconveyor1(500);
    }
    public static void stopconvoyor1() {
        ConveyorMotor.Stopconveyor1();
    } public static void stopconvoyor1Umergency() {
        ConveyorMotor.runconveyor1(0);
    }
    public static void setspeedconvoyer1() {

    }
    //brickstucked  secs
public static void runDegsconvoyer1() {
        ConveyorMotor.runDegsconvoyer1(2,-100);
}
public static boolean brickStucked() {
        ConveyorMotor.runSecsconvoyer1(-50);
    return true;
}

    /*public static void convoyormotor(){
        ConveyorMotor.start(-100);
        ConveyorMotor.startSlaow(-20);
        ConveyorMotor.Stop();
        }
          /*
        brickstucked &&& rundegs
         */
     //********** ConveyorMotor 2 ***********

    public static void stopconvoyor2() {
        Conveyormotor.Stopconveyor2();
    }

    //********** Shreder Motor ***********

    public static void startshrederMotor() {
        ShredderMotor.runShredermotorstart(800);
          }
    public static void re_startshrederMotor() {
        ShredderMotor.re_runShredermotor(500);
    }
    public static void stoptshrederUmergency() {
        ShredderMotor.runShredermotorstart(0);
    }
    public static void stoptshrederMotor() {
        ShredderMotor.stopShredder();
    }
    //****************** Ultrasonic Sensor ****************
    public static int ultrasonicSensor() {

      //  UltrasonicSensor.isDanger();
        return UltrasonicSensor.isDanger();
    }
    public static void colorSensor(){
        ColorSensor.waitBrick();
        //ColorSensor.readcolor();
          }
          public static int waitBrick() {
         return ColorSensor.decideColor();

          }
    //************************* Drop Motor ***************
    public static void dropmotorReset() {

        DropMotor.resetPosition(40,0);

          }
          public static void  dropMotor() {
             DropMotor.dropup();
          }
        public static void dropdown() {
            DropMotor.dropdown();

        }
          public static void vcolor() {
               }

    public static void FirstStartconvoyor2() {
        Conveyormotor.GoBluePosition(800);
    }

    public static void SecondStartconvoyor2() {
        Conveyormotor.GoGreenPosition(800);
    }

    public static void ThirdStartconvoyor2() {
        Conveyormotor.GoYelloPosition(800);
    }
    public static void FourthStartconvoyor2() {
        Conveyormotor.GoRedPosition(800);
    }
    public static void runConveyor2() {
        Conveyormotor.runconvoyer(800);
    }
}


























/*

    public static void est() {
        ColorSensor.waitBrick();
    }

    public static void motorA(){
    m.runMotor(500);

    }



    public static void init1(){
        if (true){

        }

    }
    public static void confs1(final EV3ColorSensor color){

    }
    public void config() {
        final EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        System.out.println("Color detect");
        color.getRGBMode();
        int number =color.getColorID();
        if (number== Color.RED){
            System.out.println("Red Detected");
            m.runMotor(400);
        }
        else if (number==Color.BLUE) {
            System.out.println("Blue Detected");
            m.runMotor(800);

        }
        else if (number==Color.YELLOW) {
            System.out.println("Yellow Detected");
            m.runMotor(1000);

        }
        else if (number==Color.GREEN) {
            System.out.println("Green Detected");
        }
        else {
            System.out.println("No Color");
            m.Stop();
        }
    }
}
*/