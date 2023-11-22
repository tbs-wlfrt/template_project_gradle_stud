package OurCode.Helpers;


import lejos.utility.Delay;

public class PIDController {
    double[] vals = new double[2];

    int setPoint = 15;

    double kp = 0.8F;//1.6F; // try 1.6
    double ki = 0.00001F;
    double kd = 1.6F;//3.2F;

    // define error vars
    int lastError1 = 0;
    int cumError1 = 0;

    // time (ms)
    long previousTime = 0;

    // constructor
    public PIDController(int distance, double proportionality){
        // define how far we want the robot to keep its distance
        setPoint = distance;
        previousTime = System.currentTimeMillis();
    }

    int recalibrate(){
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;

        int distance_error1 = 0;
        if (vals[0] > 5 && vals[0] <= 100){
            distance_error1 = (int) (vals[0] - setPoint);
        }
        cumError1 += (distance_error1*elapsedTime);

        int rateError1 = (int) ((distance_error1 - lastError1)/elapsedTime);

        int output1 = (int) (kp * distance_error1 + ki * cumError1 + kd * rateError1);

        lastError1 = distance_error1;
        previousTime = currentTime;

        if (distance_error1 == 0){
            return 0;
        }

        return output1;
    }

    // update the error tuple
    void updateVals(double currDistance){
        // set the current most recent to the old value
        vals[0] = vals[1];
        // set the newly read value as the most recent
        vals[1] = currDistance;
    }



    //need method for: updating values, outputting new values

}