package Layer2_Dev;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.CONFIRM;

public class PushAgent extends Agent {

        @Override
        public void setup() {
            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    ACLMessage message = receive();
                    if (message!=null) {
                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {

                                try {
                                    dev2.pushproduct();
                                    System.out.println("pushed");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {

                                try {
                                    Delay.msDelay(2000);
                                    ACLMessage message = new ACLMessage(CONFIRM);
                                    message.addReceiver(new AID("SortAgent2", AID.ISLOCALNAME));
                                    message.setContent("done");
                                    send(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                }
                });
        }
}
