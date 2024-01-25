package OurCode.UWB.pozyx;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.TimeUnit;

public class main {

    public static void main(String[] args) throws MqttException, InterruptedException {

        TagIdMqtt tag = new TagIdMqtt("685C");
// 685C to decimal =
        while (true)
        {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("tag.getLocation() = " + tag.getLocation());
            System.out.println("tag.getOrientation() = " + tag.getOrientation());
        }
    }

}