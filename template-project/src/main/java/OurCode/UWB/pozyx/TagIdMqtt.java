package OurCode.UWB.pozyx;

import OurCode.UWB.helpers.Point2D;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TagIdMqtt {
    // Address of online pozyx broker
    private final String host = "wss://mqtt.cloud.pozyxlabs.com:443";
    private final String topic = "61d730870295a7f3798fdb31";
    private final String username = "61d730870295a7f3798fdb31";
    private final String password = "1d761f94-6fe7-4549-aaa5-73a4ffecc2ee";

    // Address of pozyx broker on server PC
//    private final String host = "tcp://168.0.105:1883";

    // address of pozyx broker local
//    private final String host = "tcp://localhost:1883";
//    private final String topic = "tags";

    private MqttClient client;
    private final String tagId;
    private final Object lock = new Object();
    private boolean new_message = false;
    private volatile JSONObject last_message;
    volatile Point2D notSmoothened;
    public final List<Point2D> locations = new ArrayList<>();
    public float orientation;

    public TagIdMqtt(String tagId) throws MqttException {
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
        while (listIterator.hasPrevious() && count < window) {
            smoothened = smoothened.add(listIterator.previous());
            count++;
        }
        if (count == 0)
            count = 1;
        smoothened = smoothened.div(count);

        return smoothened;
    }

    public Point2D getLocation() {
        return notSmoothened;
    }
    public float getOrientation(){
        return orientation;
    }

    private MqttClient create_client() throws MqttException {
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
            System.out.println("Connected...");
            client.subscribe(topic);


            return client;
        } catch (MqttException me) {
            print_exception(me);
            throw me;
        }
    }

    private void print_exception(MqttException me) throws MqttException {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
    }

    private class Callback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {
            System.out.println(cause);
//            cause.printStackTrace();
//            System.exit(0);
            try {
                client = create_client();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            JSONArray jsonObject = new JSONArray(message.toString());
            synchronized (lock) {
                try {
                    for (int i = 0; i < jsonObject.length(); i++) {
                        // get the object that matches the tagId
                        JSONObject json = new JSONArray(message.toString()).getJSONObject(i);
                        if (jsonObject.getJSONObject(i).getInt("tagId") == Integer.parseInt(tagId, 16)) {
                            if (Integer.parseInt(tagId, 16) == json.getInt("tagId")) {
                                if (json.getBoolean("success")) {
                                    new_message = true;
                                    JSONObject coordinates = json.getJSONObject("data").getJSONObject("coordinates");
                                    JSONObject orientation_ = json.getJSONObject("data").getJSONObject("orientation");
                                    locations.add(new Point2D(coordinates.getInt("x"), coordinates.getInt("y")));
                                    notSmoothened = new Point2D(coordinates.getInt("x"), coordinates.getInt("y"));
                                    orientation = orientation_.getFloat("yaw");
                                    lock.notify();

                                } else if (!json.getBoolean("success")) {
                                    new_message = false;
                                }
                            }
                        }
                    }
                } catch (JSONException ignored) {
                    System.out.println("Malformed message received: " + message);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("Delivery complete");
        }

    }

}
