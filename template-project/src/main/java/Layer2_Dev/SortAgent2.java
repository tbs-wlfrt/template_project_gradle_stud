package Layer2_Dev;

import Layer1_Dev.dev1;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.CONFIRM;
import static jade.lang.acl.ACLMessage.INFORM;


public class SortAgent2 extends Agent {
    @Override
    public void setup() {

                ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
                addBehaviour(parallelBehaviour);
                parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
                    @Override
                    public void action() {
                        ACLMessage Message=receive(MessageTemplate.MatchPerformative(CONFIRM));
                        if (Message!=null) {
                            Delay.msDelay(5000);
                            int color= dev1.waitBrick();

                            if (color==2) {
                                dev1.FirstStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==3) {
                                dev1.SecondStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==4) {
                                dev1.ThirdStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==5) {
                                dev1.FourthStartconvoyor2();
                                System.out.println("pass");
                                Delay.msDelay(3500);
                                redBrick();
                            }
                            else {
                                RestartCheking(); /** Restart the function If the color is wrong */
                            }
                        }

                    }
                });
                parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
                    @Override
                    public void action() {

                        try {
                            Delay.msDelay(15000);
                            int color= dev1.waitBrick();

                            if (color==2) {
                                dev1.FirstStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==3) {
                                dev1.SecondStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==4) {
                                dev1.ThirdStartconvoyor2();
                                Delay.msDelay(2000);
                                normalBrick();
                            }
                            if (color==5) {
                                dev1.FourthStartconvoyor2();
                                System.out.println("pass");
                                Delay.msDelay(3500);
                                redBrick();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


    }
    public   void normalBrick() {
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID("PushAgent", AID.ISLOCALNAME));
        messageTemplate.setContent("Brick received");
        send(messageTemplate);

    }
    public  void redBrick() {
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID("BuildAgent", AID.ISLOCALNAME));
        messageTemplate.setContent("Brick received");
        System.out.println("cc");
        send(messageTemplate);

    }
public void RestartCheking() {
    Delay.msDelay(15000);
    int color= dev1.waitBrick();

    if (color==2) {
        dev1.FirstStartconvoyor2();
        Delay.msDelay(2000);
        normalBrick();
    }
    if (color==3) {
        dev1.SecondStartconvoyor2();
        Delay.msDelay(2000);
        normalBrick();
    }
    if (color==4) {
        dev1.ThirdStartconvoyor2();
        Delay.msDelay(2000);
        normalBrick();
    }
    if (color==5) {
        dev1.FourthStartconvoyor2();
        System.out.println("pass");
        Delay.msDelay(3500);
        redBrick();
    }
}
          /* int color=dev1.waitBrick();

                    if (color==2) {
                          dev1.FirstStartconvoyor2();
                        normalBrick();
                       }
                    if (color==3) {
                        dev1.SecondStartconvoyor2();
                        Delay.msDelay(1000);
                        normalBrick();
                    }
                    if (color==4) {
                        dev1.ThirdStartconvoyor2();
                        normalBrick();
                    }
                    if (color==5) {
                        dev1.FourthStartconvoyor2();
                        System.out.println("pass");
                        Delay.msDelay(3000);
                        redBrick();
                    }
                   }
*/
}
