package Layer1_Dev;

import Layer2_Dev.InitComp2;
import lejos.utility.Delay;

public class Test {
    public static void main( String[] args){
        //dev1.dropMotor();
       // Delay.msDelay(1);
      //  Delay.msDelay(1000);
     //   dev1.dropmotorReset();
        /*   dev1.startslaowconvoyor1();
        Delay.msDelay(1);
        dev1.startshrederMotor();*/
        InitComp2.initComponents();
        while (true){

            Delay.msDelay(10000);
           int doh= dev1.waitBrick();
            if (doh==2) {
            dev1.FirstStartconvoyor2();
            }
            if (doh==3) {
                dev1.SecondStartconvoyor2();

            }
            if (doh==4) {
                dev1.ThirdStartconvoyor2();

            }
            if (doh==5){
               dev1.FourthStartconvoyor2();
            }

}
    }
}
