package OurCode.Helpers;

public class ColorPIDController {

    float val;//s = new float[2];

    public int setPoint = 33;
    public int fullBlack = 5;           //Lower bound for the sensor reading all black
    public int fullWhite = 500;           //Upper bound for the sensor reading all white

    // sensitivity to the current measurement
    double kp = 0.4; //3.60F;//1.6F; // try 1.6
    double ki = 0.00001F;
    double kd = 3.2;//20.2F;//3.2F;

    // define error vars
    int lastError1 = 0;
    int lastError2 = 0;
    int cumError1 = 0;
    int cumError2 = 0;

    // time (ms)
    long previousTime = 0;

    // constructor
    public ColorPIDController(int set) {
        // define how far we want the robot to keep its distance
        setPoint = set;
        previousTime = System.currentTimeMillis();
    }

    public int[] recalibrate(float currColor) {
        val = currColor;

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - previousTime;

        int error1 = 0;
        int error2 = 0;

        if (val < 5) { // don't change anything (polling too fast)
            return new int[]{0, 0};
        }


        // range black [5, 33]
        if (val >= fullBlack && val <= setPoint) {
            error1 = (int) (setPoint - val);
        }

        if (val > setPoint && val <= fullWhite) {
            error2 = (int) (setPoint - val);
        }

        cumError1 += error1 * elapsedTime;
        cumError2 += error2 * elapsedTime;

        int rateError1 = (int) ((error1 - lastError1) / elapsedTime);
        int rateError2 = (int) ((error2 - lastError2) / elapsedTime);

        int output1 = (int) (kp * error1 + ki * cumError1 + kd * rateError1);
        int output2 = (int) (kp * error2 + ki * cumError2 + kd * rateError2);

        lastError1 = error1;
        lastError2 = error2;

        previousTime = currentTime;

        if (output1 < 0)
            output1 = output1 * -1;

        if (output2 < 0)
            output2 = output2 * -1;

        return new int[]{output1, output2};
    }

    // update the error tuple
    void updateVal(float currDistance) {
        // set the current most recent to the old value
        //vals[0] = vals[1];
        // set the newly read value as the most recent
        val = currDistance;
    }

    public int getSetPoint() {
        return setPoint;
    }

}