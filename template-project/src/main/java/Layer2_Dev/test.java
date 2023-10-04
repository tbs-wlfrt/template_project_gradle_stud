package Layer2_Dev;

import lejos.utility.Delay;

import java.util.Random;

public class test {

    public static void main(String[] args) {
 /*       InitComp2.initComponents();
        Delay.msDelay(20000);
      '  dev2.Eject();*/
while (true){
        int number = 0;
        Random rd = new Random();

        for (int counter =1; counter<10; counter++){
            number= 1+rd.nextInt(8);
        }
    Delay.msDelay(5000);
     if (number == 1) {
        System.out.println("one");
    }
            if (number == 2) {
        System.out.println("Two");
    }
            if (number == 3) {
        System.out.println("three");
    }
            if (number == 4) {
        System.out.println("foor");
    }
else {
                System.out.println("false");
                test();
            }
 }

    }
public static void test() {
    int number = 0;
    Random rd = new Random();

    for (int counter =1; counter<2; counter++){
        number= 1+rd.nextInt(8);
    }

    if (number == 1) {
        System.out.println("one");
    }
    if (number == 2) {
        System.out.println("Two");
    }
    if (number == 3) {
        System.out.println("three");
    }
    if (number == 4) {
        System.out.println("foor");
    }
}
}
