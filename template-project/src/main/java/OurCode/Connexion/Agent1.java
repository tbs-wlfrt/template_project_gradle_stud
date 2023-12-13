package OurCode.Connexion;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;
import static jade.lang.acl.ACLMessage.INFORM;
import jade.core.AID;

import java.util.Objects;


public class Agent1 extends Agent {
    String RobotAgentAPI = "RobotAgent@192.168.0.158:1099/JADE";

    //String RobotAgentAPI = "RobotAgent";
    @Override
    protected void setup() {
        System.out.println("local name" + getAID().getLocalName());
        System.out.println("GloBal name" + getAID().getName());

        /*
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID(RobotAgentAPI,AID.ISGUID));

         */
        addBehaviour(message_recieve);

    }

    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at agent1:" + msg.getContent());
                //TODO: insert logic for reacting to messages
                switch (msg.getContent()){
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
                        String robotPath = "RRR";
                        send_message(robotPath);
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
                /*
                //example reactive logic: just acknowledge message recieved
                ACLMessage messageTemplate = new ACLMessage(INFORM);
                messageTemplate.addReceiver(new AID(RobotAgentAPI, AID.ISGUID));
                messageTemplate.setContent("Acknowledge message: " + msg.getContent());
                send(messageTemplate);

                 */

            }
        }
    };

    private void send_message(String content){
        ACLMessage msg = new ACLMessage(INFORM);
        msg.addReceiver(new AID(RobotAgentAPI, AID.ISGUID));
        msg.setContent(content);
        send(msg);
    }

}

