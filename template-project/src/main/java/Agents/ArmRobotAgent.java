package Agents;

import RobtArm.devRobot;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;


public class  ArmRobotAgent extends Agent {
    @Override
    public void setup() {
        /**********
                      Recieve tasks
                                     ************/

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage = receive();
                if (aclMessage != null) {
                    int number = Integer.parseInt(aclMessage.getContent());

                    /**********
                                  Blue color sequence
                                                      ************/

                    if (number == 0) {

                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                       /************  Go to canal 1 ***********/
                                try {
                                    devRobot.goCanal1();
                                    System.out.println("Step 1 : Go to canal ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                      /************  Descent for product ***********/

                                try {
                                    devRobot.descentForProduct();
                                    System.out.println(" Step 2 : Descent for product");
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

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new Behaviour() {
                            @Override
                            public void action() {                     /************ Check Color Product***********/
                                try {
                                    devRobot.checkprodect();
                                    System.out.println(" Step 3 : Check Color Product");
                                    int colorValue = devRobot.checkprodect();
                                    if (colorValue == 2) {
                                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Input PL **********/
                                                try {
                                                    devRobot.turnToIntput();
                                                    System.out.println("Step 6 : Turn to Input prod");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Intput **********/
                                                try {
                                                    devRobot.descentForIntput();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    } else {
                                        System.out.println("wrong color");
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Bag **********/
                                                try {
                                                    devRobot.TurntoBag();
                                                    System.out.println("Step 6 : Turn to Bag");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Bag **********/
                                                try {
                                                    devRobot.descentForProduct();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

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

                            @Override
                            public boolean done() {
                                int colorValue = devRobot.checkprodect();
                                if (colorValue != 0) {
                                    System.out.println("Color Id : " + colorValue);
                                    return true;
                                }

                                return false;
                            }
                        });
                    }


                        /**********
                                    Green color sequence
                                                            ************/


                    if (number == 1) {

                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                       /************  Go to canal 2 ***********/
                                try {
                                    devRobot.goCanal2();
                                    System.out.println("Step 1 : Go to canal ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                      /************  Descent for product ***********/

                                try {
                                    devRobot.descentForProduct();
                                    System.out.println(" Step 2 : Descent for product");
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

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new Behaviour() {
                            @Override
                            public void action() {                     /************ Check Color Product***********/
                                try {
                                    devRobot.checkprodect();
                                    System.out.println(" Step 3 : Check Color Product");
                                    int colorValue = devRobot.checkprodect();
                                    if (colorValue == 3) {
                                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Input PL **********/
                                                try {
                                                    devRobot.turnToIntput();
                                                    System.out.println("Step 6 : Turn to Input prod");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Intput **********/
                                                try {
                                                    devRobot.descentForIntput();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    } else {
                                        System.out.println("wrong color");
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Bag **********/
                                                try {
                                                    devRobot.TurntoBag();
                                                    System.out.println("Step 6 : Turn to Bag");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Bag **********/
                                                try {
                                                    devRobot.descentForProduct();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

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

                            @Override
                            public boolean done() {
                                int colorValue = devRobot.checkprodect();
                                if (colorValue != 0) {
                                    System.out.println("Color Id : " + colorValue);
                                    return true;
                                }

                                return false;
                            }
                        });
                    }

                    /**********
                                   Red color sequence
                                                         ************/
                  if (number == 2) {

                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                       /************  Go to canal 3 ***********/
                                try {
                                    devRobot.goCanal3();
                                    System.out.println("Step 1 : Go to canal ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                      /************  Descent for product ***********/

                                try {
                                    devRobot.descentForProduct();
                                    System.out.println(" Step 2 : Descent for product");
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

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                        comportementSequentiel.addSubBehaviour(new Behaviour() {
                            @Override
                            public void action() {                     /************ Check Color Product***********/
                                try {
                                    devRobot.checkprodect();
                                    System.out.println(" Step 3 : Check Color Product");
                                    int colorValue = devRobot.checkprodect();
                                    if (colorValue == 5) {
                                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Input PL **********/
                                                try {
                                                    devRobot.turnToIntput();
                                                    System.out.println("Step 6 : Turn to Input prod");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Intput **********/
                                                try {
                                                    devRobot.descentForIntput();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    } else {
                                        System.out.println("wrong color");
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Bag **********/
                                                try {
                                                    devRobot.TurntoBag();
                                                    System.out.println("Step 6 : Turn to Bag");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Bag **********/
                                                try {
                                                    devRobot.descentForProduct();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

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

                            @Override
                            public boolean done() {
                                int colorValue = devRobot.checkprodect();
                                if (colorValue != 0) {
                                    System.out.println("Color Id : " + colorValue);
                                    return true;
                                }

                                return false;
                            }
                        });
                    }

                    /**********
                                         Yellow color sequence
                                                                  ************/
                    if (number == 3) {

                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
                        addBehaviour(comportementSequentiel);
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                       /************  Go to canal 4 ***********/
                                try {
                                    devRobot.goCanal4();
                                    System.out.println("Step 1 : Go to canal ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                            @Override
                            public void action() {                      /************  Descent for product ***********/

                                try {
                                    devRobot.descentForProduct();
                                    System.out.println(" Step 2 : Descent for product");
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

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        comportementSequentiel.addSubBehaviour(new Behaviour() {
                            @Override
                            public void action() {                     /************ Check Color Product***********/
                                try {
                                    devRobot.checkprodect();
                                    System.out.println(" Step 3 : Check Color Product");
                                    int colorValue = devRobot.checkprodect();
                                    if (colorValue == 3) {
                                        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Input PL **********/
                                                try {
                                                    devRobot.turnToIntput();
                                                    System.out.println("Step 6 : Turn to Input prod");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Intput **********/
                                                try {
                                                    devRobot.descentForIntput();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                    } else {
                                        System.out.println("wrong color");
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {           /***************Close gripper **********/
                                                try {
                                                    devRobot.closedgripper();
                                                    System.out.println("Step 4: Close gripper ");

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

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Go up **********/
                                                try {
                                                    devRobot.asent();
                                                    System.out.println("Step 5 : Go up ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {                /***************Turn to Bag **********/
                                                try {
                                                    devRobot.TurntoBag();
                                                    System.out.println("Step 6 : Turn to Bag");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Descent For Bag **********/
                                                try {
                                                    devRobot.descentForProduct();
                                                    System.out.println("Step 7 : Descent For Intput");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** open gripper **********/
                                                try {
                                                    devRobot.opengripper();
                                                    System.out.println("Job done ");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
                                            @Override
                                            public void action() {               /*************** Reset all positions **********/
                                                try {
                                                    devRobot.resetposhorArm();
                                                    System.out.println("Reset 1  ");

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

                            @Override
                            public boolean done() {
                                int colorValue = devRobot.checkprodect();
                                if (colorValue != 0) {
                                    System.out.println("Color Id : " + colorValue);
                                    return true;
                                }

                                return false;
                            }
                        });
                    }
                 /**   else {
                        System.out.println("No product in the canal");
                    }*/

                }  //end of message content
            }   // end action Cyclic Behaviour
             });
    }
}
