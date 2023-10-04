package Layer1_Dev;

public class Button {
    public static boolean isPressed() {
     //   EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
        boolean touch = InitComp.touchSensor.isPressed();
       // return true;
      //  System.out.println(touch);
          return touch;

    }
}
