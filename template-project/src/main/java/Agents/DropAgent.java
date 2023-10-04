package Agents;
import Layer1_Dev.dev1;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.INFORM;

public class DropAgent extends Agent  {
    @Override
    public void setup() {
           addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
              //  MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);
                ACLMessage Message=receive();
                if (Message!=null) {  /** Recieve Messages*/
                    System.out.println(Message.getContent());
                //   boolean prsenceProduct= dev1.readcolor();
                    try {
                        if (dev1.readcolor()){
                           SequentialBehaviour Sequentiel = new SequentialBehaviour();
                            addBehaviour(Sequentiel);
                            Sequentiel.addSubBehaviour(new OneShotBehaviour() {
                                @Override
                                public void action() {
                                    try {
                                       dev1.dropMotor();
                                        Delay.msDelay(1);
                                    //    System.out.println("DropAgent : product dropping");
                                        ACLMessage messageTemplate = new ACLMessage(INFORM);
                                        messageTemplate.addReceiver(new AID("ShredAgent", AID.ISLOCALNAME));
                                        messageTemplate.setContent("Dropped");
                                        send(messageTemplate);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            Sequentiel.addSubBehaviour( new OneShotBehaviour() {
                                @Override
                                public void action() {

                                    try {
                                        ACLMessage msg = new ACLMessage(INFORM);
                                        msg.addReceiver(new AID("SortAgent", AID.ISLOCALNAME));
                                        msg.setContent("received product");
                                        send(msg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    block();  /** Block CyclicBehaviour If there is no message **/
                }
            }
        });

    }


    @Override
    protected void takeDown() {
    }
}

