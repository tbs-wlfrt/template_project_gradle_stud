package UWB.mqtt;

import UWB.helpers.Point2D;
import org.eclipse.paho.client.mqttv3.MqttException;

public class TestMain {
    public static void main(String[] args) throws MqttException, InterruptedException {
        // FILL IN HEXID OF TAG (without 0x)
        TagMqtt tag = new TagMqtt("6841");
        Point2D last_loc = new Point2D(0,0);
        Point2D dest = new Point2D(-151, 4242);
        while (true) {
            synchronized (tag.getLock()) {
                while (!tag.isNewMessage()) {
                    try {
                        tag.getLock().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            }
            Point2D loc = tag.getSmoothenedLocation(10);
            System.out.println(loc);
            float yaw = (float) Math.toDegrees(tag.yaw);
            System.out.println("yaw=***** "+yaw);
            last_loc = loc;
       //     Thread.sleep(1000);
        }
    }
}
