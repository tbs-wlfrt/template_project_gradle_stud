package RobtArm;


import lejos.hardware.sensor.SensorMode;


public class ChekProduct {

    static boolean call_mode = false;
    static SensorMode idcolor = initComp.color.getRGBMode();

    public static int decideColor()  {

        //int[] colorSamplings = new int[10];
        float colorSamplings=0;
        System.out.println("Reading is starting");
        int index = 0;
        float x =0;

        while (index < 9) {
            int idcolor = initComp.color.getColorID();
            System.out.println("clrID"+idcolor);
            colorSamplings+=idcolor;
            index = index + 1;
            }
        x= (colorSamplings)/10; // take the average.
        return Math.round(x);
        /*if (x>5) {
            return x;
        }
         else return 0;
*/
    }

    public static boolean check() {

        int idcolor = initComp.color.getColorID();
        if (idcolor == 2 || idcolor == 3 || idcolor == 4 || idcolor == 5 || idcolor == 6 || idcolor == 7) {
            System.out.println("id color " + idcolor);

            return true;

        }
        return false;
    }
      //  return false;
   // }


    public static boolean place() {
        int idcolor = initComp.color.getColorID();

        if (idcolor != 2 || idcolor != 3 || idcolor != 4 || idcolor != 5 || idcolor != 6 || idcolor != 7) {
            System.out.println("place checked");
            return true;

    }
        return false;
    }
}
