package OurCode;

public class Turner{

    /**
     * calculates by how many degrees a wheel has to turn to pivot the vehicle by given number of degrees.
     */
    public static double calculatePivot(int degrees, float wheelDiameter, float trackSize){
        double bigCircumference = 2 * trackSize * Math.PI;
        double wheelCircumference = wheelDiameter * Math.PI;
        //travel distance required for vehicle to rotate 1 degree = bigCircumference/360
        double travelDistance = (bigCircumference/360) * degrees;
        double wheelRotation = travelDistance/wheelCircumference * 360;

        return wheelRotation;
    }

    /**
     * calculates by how many degrees a both wheels have to turn to rotate the vehicle by given number of degrees in place.
     */
    public static double calculateInPlace(int degrees, float wheelRadius, float trackSize){
        double bigCircumference = trackSize * Math.PI;
        double wheelCircumference = wheelRadius * Math.PI;
        //travel distance required for vehicle to rotate 1 degree = bigCircumference/360
        double travelDistance = (bigCircumference/360) * degrees;
        double wheelRotation = travelDistance/wheelCircumference * 360;

        return wheelRotation;
    }

    public static void main(String[] args) {
        
        System.out.println(Turner.calculatePivot(4, 44, 100));
    }
}