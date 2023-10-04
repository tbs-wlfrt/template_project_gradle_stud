package UWB.mqtt;

import UWB.helpers.Point2D;
import jade.core.Agent;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TagMqtt {
    // Address of online pozyx broker
    private final String host = "wss://mqtt.cloud.pozyxlabs.com:443";
    private final String topic = "61d730870295a7f3798fdb31";
    private final String username = "61d730870295a7f3798fdb31";
    private final String password = "1d761f94-6fe7-4549-aaa5-73a4ffecc2ee";

    // Address of pozyx broker on server PC
//    private final String host = "tcp://
//    .168.0.105:1883";

    // address of pozyx broker local
//    private final String host = "tcp://localhost:1883";
//    private final String topic = "tags";


    private final MqttClient client;
    private final String tagId;

    private final Object lock = new Object();

    private boolean new_message = false;

    private volatile JSONObject last_message;

    public final List<Point2D> locations = new ArrayList<>();
    public final List<Float> yaws = new ArrayList<>();
    public float yaw;

    public TagMqtt(String tagId) throws MqttException {
        this.tagId = tagId;
        client = create_client();
        
    }

    public Object getLock() {
        return lock;
    }

    public void shutdown() throws MqttException {
        client.disconnect();
        System.out.println("Disconnected");
        client.close();
    }

    public JSONObject getLastMessage() {
        new_message = false;
        return last_message;
    }

    public boolean isNewMessage() {
        return new_message;
    }

    public Point2D getSmoothenedLocation(int window) {
        new_message = false;

        Point2D smoothened = new Point2D(0, 0);

        int count = 0;
        ListIterator<Point2D> listIterator = locations.listIterator(locations.size());
        while(listIterator.hasPrevious() && count < window) {
            smoothened = smoothened.add(listIterator.previous());
            count++;
        }
        if (count == 0)
            count = 1;
        smoothened = smoothened.div(count);

        return smoothened;
    }

    public Point2D getLastLocation() {
        new_message = false;
        return locations.get(locations.size() - 1);
    }

    private MqttClient create_client() throws MqttException {
        System.out.println("mqtt client created.");
        return create_client(host, topic, username, password);
    }

    private MqttClient create_client(String host, String topic, String username, String password) throws MqttException {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient client = new MqttClient(host, MqttClient.generateClientId(), persistence);

            // MQTT connection option
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            // retain session
            connOpts.setCleanSession(true);

            // set callback
            client.setCallback(new Callback());

            // establish a connection
            System.out.println("Connecting to broker: " + host);
            client.connect(connOpts);
            System.out.println("Connected");

            client.subscribe(topic);

            return client;
        } catch (MqttException me) {
            print_exception(me);
            System.out.println("exp22");
            throw me;
        }

    }

    private void print_exception(MqttException me) throws MqttException {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
        System.out.println("exp33");
    }

    private class Callback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("exp44");
            System.out.println(cause);



            //
         //   System.exit(0);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            synchronized (lock) {
                try {
                    JSONObject json = new JSONArray(message.toString()).getJSONObject(0);
                    if (Integer.parseInt(tagId, 16) == json.getInt("tagId") && json.getBoolean("success")) {
                        last_message = json;
                        new_message = true;
                        JSONObject coor = last_message.getJSONObject("data").getJSONObject("coordinates");
                        JSONObject ori = last_message.getJSONObject("data").getJSONObject("orientation");

                        locations.add(new Point2D(coor.getInt("x"), coor.getInt("y")));

//                        if(yaws.size() > 9)
//                            yaws.remove(0);
//                        yaws.add(ori.getFloat("yaw"));
//                        yaw = 0;
//                        for(int i = 0; i < yaws.size(); i++) {
//                            yaw += yaws.get(i);
//                        }
//                        yaw /= yaws.size();
                        yaw = ori.getFloat("yaw");

                        lock.notify();
                    } else if (!json.getBoolean("success")) {
                        System.out.println(json);
                    }
                }
                catch (JSONException ignored) {
                    System.out.println("Mallformed message received: " + message);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("Delivery complete");
        }
    }
}
