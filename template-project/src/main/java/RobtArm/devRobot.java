package RobtArm;

public class devRobot {
    private static ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor EV3LargeRegulatedMotor;
    private static ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor EV3MediumRegulatedMotor;

    public static void goCanal1() {
        VerticalArm.turnleft1(50);
    }
    public static void goCanal2() {
        VerticalArm.turnleft2(50);
    }

    public static void goCanal3() {
       VerticalArm.turnleft3(50);
    }
    public static void goCanal4() {
        VerticalArm.turnleft4(50);
    }

    public static void verleft3() {

        VerticalArm.turnleft3(50);
    }
    public static void verleft4() {

        VerticalArm.turnleft4(50);
    }
    public static void verright1() {
        VerticalArm.turnright1(150);
    }
    public static void turnToIntput() {
        VerticalArm.resetPos(100);
    }
    public static void verright2() {

          VerticalArm.turnright2(250);

    }
    public static void verright3() {

        VerticalArm.turnright3(250);

    }
    public static void verrigh4t() {

         VerticalArm.turnright4(100);
    }
    public static void gripper() {
        GripperMotor.colosed(100);
    }
    public static void asent() {
        HorizontaleArm.goUp(50);
    }
    public static void descentForProduct() {
        HorizontaleArm.goDown(50);
    }
    public static void descentForIntput() {
        HorizontaleArm.goDown(50);
    }
    public static void opengripper() {
        GripperMotor.open(50);
    }
    public static void closedgripper() {
        GripperMotor.colosed(150);
    }
    public static void resetposverArm() {
        VerticalArm.resetPos(50);
    }
    public static void resetposhorArm() {
       HorizontaleArm.resetPos(0);
    }
    public static int checkprodect() {
      return ChekProduct.decideColor();
    }
    public static void TurntoBag() {
        VerticalArm.dropproduct(50);
    }
    public static boolean checkplace() {
        return ChekProduct.place();
    }

    public static void init_positions() {
        VerticalArm.resetPos(100);
        HorizontaleArm.resetPos(100);
        GripperMotor.resetPos();
    }
}
