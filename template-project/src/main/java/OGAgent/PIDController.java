package OGAgent;


public class PIDController {

    float val;//s = new float[2];

    int setPoint = 76;

    double kp = 3.60F;//1.6F; // try 1.6
    double ki = 0.00001F;
    double kd = 20.2F;//3.2F;

    // define error vars
    int lastError1 = 0;
    int lastError2 = 0;
    int cumError1 = 0;
    int cumError2 = 0;

    // time (ms)
    long previousTime = 0;

    // constructor
    PIDController(int set){
        // define how far we want the robot to keep its distance
        setPoint = set;
        previousTime = System.currentTimeMillis();
    }

    int[] recalibrate(float currDistance){
        val = currDistance;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;

        int error1 = 0;
        int error2 = 0;
        if (val > 20 && val <= 75){
            error1 = (int) (val - setPoint);
        }

        if (val > 75 && val <= 156){
            error2 = (int) (val - setPoint);
        }

        cumError1 += error1*elapsedTime;
        cumError2 += error2*elapsedTime;

        int rateError1 = (int) ((error1 - lastError1)/elapsedTime);
        int rateError2 = (int) ((error2 - lastError2)/elapsedTime);


        int output1 = (int) (kp * error1 + ki * cumError1 + kd * rateError1);
        int output2 = (int) (kp * error2 + ki * cumError2 + kd * rateError2);


        lastError1 = error1;
        lastError2 = error2;

        previousTime = currentTime;

        /*
        if (error1 == 0){
            return 0;
        }

        */

        return new int[]{output1, output2};
    }

    // update the error tuple
    void updateVal(float currDistance){
        // set the current most recent to the old value
        //vals[0] = vals[1];
        // set the newly read value as the most recent
        val = currDistance;
    }



    //need method for: updating values, outputting new values

}