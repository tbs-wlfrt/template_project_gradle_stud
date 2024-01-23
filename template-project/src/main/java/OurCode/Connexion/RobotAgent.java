package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.ColorPIDController;
import OurCode.Helpers.ColorPIDController_copy;
import OurCode.Helpers.LocationMessageParser;
import OurCode.Helpers.PIDController;
import OurCode.UWB.helpers.Point2D;
import UWB.mqtt.TagMqtt;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.works.debugger.events.DBEventLocation;
import org.eclipse.paho.client.mqttv3.MqttException;
import OurCode.UWB.pozyx.TagIdMqtt;


import java.util.concurrent.TimeUnit;

import static jade.lang.acl.ACLMessage.INFORM;

public class RobotAgent extends Agent {
    int[] path = new int[]{}; // a list containing the path the agent has to take
    int delay = 5000; // the delay between each step the agent takes
    boolean obstacleExists = false; // whether the agent is an obstacle or not
    int obstacleDistanceThreshold = 25; // the maximum acceptable distance required between the agent and the obstacle
    float steerCorrection = 0.07f;//0.08f;

    ColorPIDController_copy colorPID = new ColorPIDController_copy(50);
    PIDController distancePID = new PIDController(50);
    int speedMultiplier = 40;
    int motorsFullSpeed = 150;

    int junctionColor = 5;      //red
    Boolean atJunction = false;
    Boolean atCharging = false;
    Boolean obstacleDetected = false;
    float obstacleDist = 15;

    String jadeApi = "Agent1@192.168.0.158:1099/JADE";
    //String jadeApi = "Agent1";
    // Robot of other group
    String subscriberAPI = "ControlCenterAgent@192.168.0.159:1099/JADE";

    String currentPath = ""; // the current path the agent is taking

    static String tagID = "685C";
    static TagIdMqtt tag;

    static {
        try {
            tag = new TagIdMqtt(tagID);
            System.out.println("GOT HERE");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    protected void setup() {
        addBehaviour(message_recieve); // ticker
        addBehaviour(askNextCrate); // ask for initial location of crate
        addBehaviour(follow_line_routine);
    }

    void sendMessage(String message) {
        /*
        For the messaging:

        1)ACLMessage messageTemplate = new ACLMessage(INFORM);
        2)messageTemplate.addReceiver(new AID("AgentRobot@192.168.0.176:1099/JADE",AID.ISGUID));
        3)messageTemplate.setContent("Main Sends Message ->");
        4)send(messageTemplate);
        */
        // Send rot message
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID(jadeApi, AID.ISGUID));
        //messageTemplate.addReceiver(new AID(jadeApi,AID.ISLOCALNAME));
        messageTemplate.setContent(message);
        send(messageTemplate);
    }

    void sendLocationToSubscribers() {
        /*
        For the messaging:

        1)ACLMessage messageTemplate = new ACLMessage(INFORM);
        2)messageTemplate.addReceiver(new AID("AgentRobot@192.168.0.176:1099/JADE",AID.ISGUID));
        3)messageTemplate.setContent("Main Sends Message ->");
        4)send(messageTemplate);
        */

        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID(subscriberAPI, AID.ISGUID));
        //messageTemplate.addReceiver(new AID(jadeApi,AID.ISLOCALNAME));

        // send message to subscribers
        Point2D loc = tag.getLocation();
        //orientation = tag.getOrientation();
        String info = LocationMessageParser.info_to_string(loc.x, loc.y, tag.getOrientation());
        sendMessage(info);

        messageTemplate.setContent(info);
        send(messageTemplate);
    }

    OneShotBehaviour askNextCrate = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("ASKING FOR CRATE PATH");
            sendMessage("NEXT_CRATE");
        }
    };

    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        // receives message from our controller
        // sends location to subscribers
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at RobotAgent: " + msg.getContent());
                //TODO: insert logic for reacting to messages
                // Robot only recves paths
                currentPath = msg.getContent();
            }

        }
    };
    OneShotBehaviour follow_line_routine = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Starting behaviour: follow_line_routine");
            Delay.msDelay(100);
            //PID controlled line following behaviour
            try {
                // detect if robot is at a junction
                atJunction = Device.sampleBackColor() == junctionColor;

                float frontDist = Device.sampleFrontDistance();
                System.out.println("Front dist:" + frontDist);
                obstacleDetected = 0 < frontDist && frontDist < obstacleDist;

                if (currentPath.isEmpty()) {
                    Device.stop();
                }
                //atCharging = (Device.sampleBackColor() == chargingColor1) || (Device.sampleBackColor() == chargingColor2);
                else if (obstacleDetected) {
                    Device.stop();
                } else if ((!atJunction) && (!atCharging)) {
                    //get light sensor reading from device
                    float sample = Device.sampleLightIntensity();
                    Device.sync(20);
                    //controller.updateVal(sample);
                    int[] motorSpeedsReduction = colorPID.recalibrate(sample);
                    //System.out.println("\nGOT: " + sample + "" + "\nMotorspeeds:" + motorSpeedsReduction[0] + ", " + motorSpeedsReduction[1]);
                    //set device motor speeds to new values calculated by PID controller
                    Device.setMotorSpeeds(motorsFullSpeed - motorSpeedsReduction[0], motorsFullSpeed - motorSpeedsReduction[1]);
                    Device.startMoveForward();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int onEnd() {
            if (obstacleDetected) {
                sendMessage("OBSTACLE");
                addBehaviour(avoid_obstacle);
            } else if ((!atJunction) && (!atCharging))
                addBehaviour(follow_line_routine);
            else
                addBehaviour(rotate_to_exit);
            return super.onEnd();
        }

    };

    //behaviour used when the robot is currently at a junction (node) and needs to turn to its exit
    OneShotBehaviour rotate_to_exit = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Starting behaviour: rotate_to_exit");
            System.out.println("atJunction: " + atJunction);
            System.out.println("atCharging: " + atCharging);
            System.out.println("detected Colour: " + Device.sampleBackColor());
            Device.stop();
            try {
                tag = new TagIdMqtt(tagID);
                System.out.println("GOT HERE");
            } catch (MqttException e) {
                e.printStackTrace();
            }
            block(100);
            // turn device based on the current path
            float lightIntensity;
            float startOri = tag.getOrientation();
            float targetOri = 0;
            if (currentPath.isEmpty()) {
                currentPath = "X";
            }
            switch (currentPath.charAt(0)) {
                case 'L':
                    System.out.println("Turning Left");
                    Device.startTurnLeft(motorsFullSpeed / 4);
                    targetOri = (startOri - ((float) Math.PI / 2)) - (steerCorrection * 10);

                    break;
                case 'R':
                    System.out.println("Turning Right");
                    Device.startTurnRight(motorsFullSpeed / 4);
                    targetOri = (startOri + ((float) Math.PI / 2) - steerCorrection) % (2 * (float) Math.PI);
                    break;
                case 'B':
                    System.out.println("Turning 180 degrees");
                    Device.startTurnLeft(motorsFullSpeed / 4);
                    targetOri = (startOri + (float) Math.PI) + (steerCorrection * 2) % (2 * (float) Math.PI);
                    break;
                default:
                    break;
            }

            if (!(currentPath.charAt(0) == 'F')) {
                if (targetOri < 0) {
                    targetOri = targetOri + (float) Math.PI * 2;
                }

                float tagOri = tag.getOrientation();
                System.out.println("Start Orientation: " + startOri);
                System.out.println("target Orientation: " + targetOri);

                boolean colCheck = true;
                boolean oriCheck = true;

                Device.sync(400);

                // given startOriDeg and tagOriDeg; keep turning until 90 degrees has been turned
                while (oriCheck && colCheck) {         //threshold of error for turning (within 10 degrees)
                    Device.sync();
                    oriCheck = !(Math.abs(tagOri - targetOri) < 0.17);
                    lightIntensity = Device.sampleLightIntensity();
                    colCheck = !(colorPID.fullBlack <= lightIntensity && lightIntensity < (colorPID.setPoint + 5));

                    tagOri = tag.getOrientation();
                    System.out.println("light intensity:" + lightIntensity);
                }
                System.out.println("colCheck: " + colCheck + "    oriCheck: " + oriCheck);

                //if stopping due to edge detection, keep rotating a little further to stop on middle of line
                if ((!colCheck) && (currentPath.charAt(0) == 'L')) {
                    Device.sync(40);
                }


            }

            Device.stop();

            // reset the values for the colorPID
            colorPID.resetValsOnTurn();
        }

        public int onEnd() {
            // pop first character of next path
            currentPath = currentPath.substring(1);

            // check if current path is empty, if so send a message to ask for NEXT_PATH
//            if (currentPath.length() == 0){
//                addBehaviour(askNextCrate); // ask for next location of crate
//            }

            atJunction = false;
            atCharging = false;
            // TODO: RE ADD THIS CHRISTOPHER NOLAN
            addBehaviour(go_forward);
            addBehaviour(follow_line_routine);
            //TODO: insert section that makes sure we dont think we are at a different junction becasue we havent moved
            return super.onEnd();
        }
    };

    OneShotBehaviour go_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.setMotorSpeeds(motorsFullSpeed, motorsFullSpeed);
                Device.moveForward(850);
                System.out.println("Forward");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    OneShotBehaviour avoid_obstacle = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                System.out.println("Avoiding obstacle");
                String response = "";
                ACLMessage msg;

                // sleep for 1 second
                block(1000);

                // try receiving message otherwise add avoid obstacle behaviour
                try {
                    msg = receive();
                    response = msg.getContent();
                    if (response.equals("WAIT")) {
                        Device.sync(50);
                        addBehaviour(follow_line_routine);
                    } else {
                        currentPath = response;
                        //follow new path
                        addBehaviour(rotate_to_exit);
                    }
                } catch (Exception e) {
                    addBehaviour(avoid_obstacle);
                    // step out of this behaviour
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int onEnd() {
            obstacleDetected = false;
            return super.onEnd();
        }
    };
}
