package Agents;

import Layer1_Dev.InitComp;
import Layer1_Dev.dev1;
import lejos.utility.Delay;

import java.rmi.RemoteException;

public class Test {
    public static void main(String[] args) throws RemoteException {
        InitComp.initComponents();
        Delay.msDelay(1000);
        dev1.startconvoyor1();

        while (dev1.Stuck() && !InitComp.motor.isMoving()) {

            System.out.println("5555555555");
            System.out.println(InitComp.motor.isStalled());
            dev1.SolveStucking();
            // Delay.msDelay(1200);
        }
    }
}




