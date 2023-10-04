package Layer1_Dev;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class ColorSensor {
    // static final EV3ColorSensor color = new EV3ColorSensor(SensorPort.S4);
//            readedcolorlist = [0]*10
    static boolean call_mode = false;


    public static void waitBrick() {

        if (!call_mode) {
            InitComp.color.getRGBMode();
          //  InitComp2.colorSensor.getRGBMode();
            call_mode = true;
        }
        int readcolrlist[] = new int[0 * 10];
       // int idcolor = InitComp.color.getColorID();
        int idcolor = InitComp.color.getColorID();

        int index = 0;
        boolean sensorOku = true;
        while (sensorOku) {
            readcolrlist[index] = idcolor;
            LocalTime now = LocalTime.now();
            DateTimeFormatter cr_time = DateTimeFormatter.ofPattern("HH:mm:ss");
            index = index + 1;
            int sum = IntStream.of(readcolrlist[index]).sum();
            int x = (sum) / 10;
            if (index == 10) {
                if (x == 3 || x == 4 || x == 5 || x == 6) {
                    System.out.printf(String.valueOf(index), cr_time, idcolor);
                    System.out.println("Retval : " + x);
                    //  dev1.retVal = x

                } else {
                    // dev1.retVal = 0.0
                }
            }
        }
    }

    public static void readcolor() {
        InitComp.color.getColorID();
        System.out.println("Color readed" + InitComp.color.getColorID());
    }

    public static int decideColor() {

        //int[] colorSamplings = new int[10];
        float colorSamplings = 0;
        System.out.println("Reading is starting");
        int index = 0;
        float x = 0;

        while (index < 9) {
            int idcolor = InitComp.color.getColorID();
            //System.out.println("clrID" + idcolor);
            colorSamplings += idcolor;
            index = index + 1;
        }
        x = (colorSamplings) / 10; // take the average.
        System.out.println("Id color = "+Math.round(x));
        return Math.round(x);

    }
}












/*public static int findSumUsingStream(int[] array) {
    return Arrays.stream(array).sum();
}*/