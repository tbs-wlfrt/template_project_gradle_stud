package Agents;

import Layer1_Dev.InitComp;
import Layer1_Dev.dev1;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;

public class ShredAgent  extends Agent {
    static boolean stop=false;
    static boolean stop1=false;
    static boolean back=false;
    public static boolean restart=false;

    @Override
    public void setup() {
      addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {

                    ACLMessage Message = receive(/*MessageTemplate.MatchPerformative(INFORM)*/); /** Receive only INFORM type messages  **/
                    if (Message != null) {
                        System.out.println(Message.getContent());
                        if (Message.getContent().equals("Brick received")){
                            stop=true;
                            stop1=true;
                            dev1.stopconvoyor1();
                            Delay.msDelay(1);
                            dev1.stoptshrederMotor();
                        }
                  if (Message.getContent().equals("Brick is stuck")){
                            back=true;
                           // InitComp.motor.resetTachoCount();
                      Delay.msDelay(10);
                   //   dev1.SolveStucking();
                      InitComp.motor1.stop();
                            Delay.msDelay(1);
                            InitComp.motor1.setSpeed(200);
                            Delay.msDelay(1);
                            InitComp.motor1.backward();
                            Delay.msDelay(1000);
                            InitComp.motor1.stop();
                            Delay.msDelay(1);
                            restart=true;
                        }
                        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
                        addBehaviour(parallelBehaviour);
                        parallelBehaviour.addSubBehaviour(new Behaviour() {
                                @Override
                                public void action() {
                                    try {
                                        //  while (true) {
                                        if (dev1.ultrasonicSensor() > 100 && Message.getContent().equals("Dropped")) {
                                            dev1.startconvoyor1();
                                            Delay.msDelay(1);
                                            dev1.startshrederMotor();
                                        } else {
                                            dev1.stopconvoyor1Umergency();
                                            Delay.msDelay(1);
                                            dev1.stoptshrederUmergency();
                                        }
                                        //     }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public boolean done() {
                                    if (stop||back) return true;
                                    else return false;
                                }
                            });
                        parallelBehaviour.addSubBehaviour(new Behaviour() {
                            @Override
                            public void action() {
                              //if (restart) {
                                try {
                                    //  while (true) {
                                    if (restart&&dev1.ultrasonicSensor() > 100 && Message.getContent().equals("Brick is stuck")) {
                                        dev1.startconvoyor1();
                                        Delay.msDelay(1);
                                        dev1.startshrederMotor();
                                    } else {
                                        dev1.stopconvoyor1Umergency();
                                        Delay.msDelay(1);
                                        dev1.stoptshrederUmergency();
                                    }

                                    //     }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                           }
                         //   }

                            @Override
                            public boolean done() {
                               if (stop1) return true;
                                else return false;
                            }
                        });
                        }

                    else {
                        block();
                    }
                }
            });
/*
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
              ACLMessage msg = receive(MessageTemplate.MatchPerformative(CONFIRM));
            if (msg!=null *//*&& msg.getContent().equals("Brick received")*//*) {
                ParallelBehaviour parallelBehaviour2 = new ParallelBehaviour();
                addBehaviour(parallelBehaviour2);
                parallelBehaviour2.addSubBehaviour(new OneShotBehaviour() {
                    @Override
                    public void action() {
                        try {
                            dev1.stoptshrederMotor();
                            Delay.msDelay(1);
                            dev1.stopconvoyor1();
                            System.out.println("Stop conveyor 1 and shredder");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            }
        });*/


        }
}

