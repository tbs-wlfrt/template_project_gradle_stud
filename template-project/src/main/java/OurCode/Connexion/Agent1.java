package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.LocationMessageParser;
import OurCode.UWB.helpers.Point2D;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;

import static jade.lang.acl.ACLMessage.INFORM;

import jade.core.AID;

import java.util.Objects;


public class Agent1 extends Agent {
    String RobotAgentAPI = "RobotAgent@192.168.0.158:1099/JADE";
    //    String RobotAgentAPI = "1@192.168.0.158:1099/JADE";
    String robotPath;
    String subscriberAPI = "ControlCenterAgent@192.168.0.159:1099";
    static String tagID = "685C";

    @Override
    protected void setup() {
        System.out.println("local name" + getAID().getLocalName());
        System.out.println("GloBal name" + getAID().getName());
        addBehaviour(message_recieve);
    }

    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at agent1:" + msg.getContent());
                switch (msg.getContent()) {
                    case "obstacle detected":
                        System.out.println("Obstacle in front of robot");
                        break;
                    case "CHARGE":
                        System.out.println("Robot has low battery, calculating path to recharge point...");
                        break;
                    case "DROPOFF":
                        System.out.println("Robot picked up crate, calculating path to drop off point...");
                        break;
                    case "TRIAGE":
                        System.out.println("Robot detected rotting crate, calculating path to triage point...");
                        break;
                    case "NEXT_CRATE":
                        System.out.println("Robot dropped off crate, calculating path to next crate...");
                        robotPath = "RFRRFRR";//"RRRRF";//"LLLLBRFRRFRR";
                        send_message(robotPath);
                        break;
                    case "OBSTACLE":
                        System.out.println("Robot detected obstacle, checking if other robot...");
                        //not implementing logic for another robot - the other controller does this logic
                        boolean otherRobot = false;
                        if (!otherRobot) {
                            //hardcoded alternate path
                            System.out.println("Sending alternate path to robot...");
                            robotPath = "BLLLBFRR";
                            send_message(robotPath);
                        }
                        break;
                    case "charging almost done":
                        System.out.println("Robot almost finished charging, nice...");
                        break;
                    case "charging done":
                        System.out.println("Robot finished charging, calculating path to next crate...");
                        break;


                    default:
                        System.out.println("Recieved unexpected message from robot: " + msg.getContent());
                        break;
                }
            }
        }
    };

    private void send_message(String content) {
        ACLMessage msg = new ACLMessage(INFORM);
        msg.addReceiver(new AID(RobotAgentAPI, AID.ISGUID));
        msg.setContent(content);
        send(msg);
    }
}

