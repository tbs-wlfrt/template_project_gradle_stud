package Layer2_Dev;

public class TouchSensor {
    public static boolean press(){

            boolean touch= InitComp2.touchSensor.isPressed();
           // System.out.println(touch);
            return touch;


    }
}
