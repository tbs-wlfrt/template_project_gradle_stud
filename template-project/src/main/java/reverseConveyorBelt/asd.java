package reverseConveyorBelt;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;


public class asd {



    public static TimerTask task = new TimerTask() {


        int counter = 0;

        public void run() {
            System.out.println("Task performed on: " + "n" +
                    "Thread's name: " + Thread.currentThread().getName());

                counter++;
            System.out.println(counter);

        }
    };


    public static void main(String[] args) {




        Timer timer = new Timer("Timer");

        long delay = 1000L;

        timer.schedule(task, 0 ,delay);


    }
}
