package Layer1_Dev;

import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
    public static int isDanger(){
        SampleProvider sp = InitComp.ultrasonicSensor.getDistanceMode();
        int distanceValue = 0;
        while (true) {
            float[] sample = new float[sp.sampleSize()];

            sp.fetchSample(sample, 0);
            distanceValue = (int) sample[0];

           // System.out.println("Distance: " + distanceValue);
         //if (distanceValue<100) {
            return distanceValue;

          }
        }
    }

    /*public static boolean isDanger() {
        //System.out.println("hhhh "+dist);
        return dist < 100;
    }*/
