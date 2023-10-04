package Layer2_Dev;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.CONFIRM;

public class BuildAgent extends Agent {
  public static int counter=0;

    @Override
    public void setup() {
addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive();
                if (message!=null) {
                 if (counter==0) {
                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {

                                try {

                                    FirstBrick();
                                    System.out.println("counter= "+counter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                    else if (counter==1) {
                     SequentialBehaviour Sequentiel = new SequentialBehaviour();
                     addBehaviour(Sequentiel);
                     Sequentiel.addSubBehaviour(new OneShotBehaviour() {
                         @Override
                         public void action() {

                             try {

                                 SecondBrick();
                                 System.out.println("counter= "+counter);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     });

                    }
                }
                else {
                    block();
                }

            }
           public void FirstBrick() {
               SequentialBehaviour comportementSequentiel1 = new SequentialBehaviour();
               addBehaviour(comportementSequentiel1);
               comportementSequentiel1.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                     dev2.Inialposition();
                       System.out.println("step 1");
                   }
               });
               comportementSequentiel1.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       dev2.Mediumpress();
                       System.out.println("step 2");
                   }
               });
               comportementSequentiel1.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       dev2.PressUp();
                       System.out.println("step 3");
                   }
               });

               comportementSequentiel1.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       System.out.println("Job done");
                       counter++;
                   }
               });
               comportementSequentiel1.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       ACLMessage messageTemplate = new ACLMessage(CONFIRM);
                       messageTemplate.addReceiver(new AID("SortAgent2", AID.ISLOCALNAME));
                       messageTemplate.setContent("done");
                       send(messageTemplate);
                   }
               });
           }
           public  void SecondBrick() {
               SequentialBehaviour comportementSequentiel2 = new SequentialBehaviour();
               addBehaviour(comportementSequentiel2);
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       dev2.PressDown1();
                       System.out.println("step 1");
                   }
               });
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       dev2.Mediumpress();
                       System.out.println("step 2");
                   }
               });
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       dev2.PressUp1();
                       System.out.println("step 3");
                   }
               });
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       Delay.msDelay(5000);
                       dev2.Eject();
                       System.out.println("step 4");
                   }
               });
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       System.out.println("Job done");
                       counter=0;
                   }
               });
               comportementSequentiel2.addSubBehaviour(new OneShotBehaviour() {
                   @Override
                   public void action() {
                       ACLMessage messageTemplate = new ACLMessage(CONFIRM);
                       messageTemplate.addReceiver(new AID("SortAgent2", AID.ISLOCALNAME));
                       messageTemplate.setContent("done");
                       send(messageTemplate);
                   }
               });
           }

        });
    }
}