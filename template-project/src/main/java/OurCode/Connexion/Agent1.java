package OurCode.Connexion;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;
import static jade.lang.acl.ACLMessage.INFORM;
import jade.core.AID;

import java.util.Objects;


public class Agent1 extends Agent {
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
                    case "low battery":
                        System.out.println("Robot has low battery, calculating path to recharge point...");
                    case "crate picked up":
                        System.out.println("Robot picked up crate, calculating path to drop off point...");
                    case "crate dropped off":
                        System.out.println("Robot dropped off crate, calculating path to next crate...");
                    default:
                        System.out.println("Revieved unexpected message from robot: " + msg.getContent());

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

}

