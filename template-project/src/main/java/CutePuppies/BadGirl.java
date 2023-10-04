package CutePuppies;

import UWB.mqtt.TagMqtt;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Random;

import static jade.lang.acl.ACLMessage.INFORM;

public class BadGirl extends Agent {
    int[] path = new int[]{};
    int path_iterator = 0;
    int delay = 5000;
    boolean obstacle = false;
    int thres_dist = 25;
    boolean in_transit = false;
    String mission_type = "";
    String message = "ack";

    static TagMqtt tag;   //682E  -> 6a75 // 6841 6841

    static {
        try {
            tag = new TagMqtt("6841");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    static int ultra_front =0;
   static int ultra_left =0;
   static int ultra_right =0;



    CyclicBehaviour obstacle_detection = new CyclicBehaviour() {
        @Override
        public void action() {
            try {
                int freeDistance = Device.lookAhead_front();
                obstacle = freeDistance < thres_dist;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    CyclicBehaviour obstacle_check = new CyclicBehaviour() {
        @Override
        public void action() {
            try {
             ultra_front = Device.lookAhead_front();
             ultra_left = Device.lookAhead_left();
             ultra_right = Device.lookAhead_right();

             //   System.out.println(" LEFT "+ultra_left+" "+" RIGHT "+ultra_right);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour sendRot = new OneShotBehaviour() {
        @Override
        public void action() {

            System.out.println("Rotten runs");
          //  while (in_transit){
                // Random number
                Random rand = new Random();
                int upperbound = 25;
                int int_random = rand.nextInt(upperbound);
                if (int_random == 10){
//                     Send a rot message
               //     ACLMessage messageTemplate = new ACLMessage(INFORM);
               //     messageTemplate.addReceiver(new AID("R2@192.168.0.136:1099/JADE",AID.ISGUID));
              //      messageTemplate.setContent("rotten");
               //     send(messageTemplate);
             //       System.out.println("Rotten");
                }
           // }
        }
    };

    TickerBehaviour lowBattery = new TickerBehaviour(this, 100) {
        @Override
        protected void onTick() {
            // Random number
            Random rand = new Random();
            int upperbound = 10;
            int int_random = rand.nextInt(upperbound);
            if (int_random == 0){
                // Send a low battery message
             //   ACLMessage messageTemplate = new ACLMessage(INFORM);
            //    messageTemplate.addReceiver(new AID("R2@192.168.0.136:1099/JADE",AID.ISGUID));
            //    messageTemplate.setContent("battery");
           //     send(messageTemplate);


           //     System.out.println("Low battery");
            }
        }
    };

    OneShotBehaviour go_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.forward();
                System.out.println("Forward");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour go_backward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.backward();
                System.out.println("Backward");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour turn_right = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.turnLeft();
                System.out.println("Right");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour turn_left = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.turnRight();
                System.out.println("Left");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour stop = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.stop();
//                System.out.println("Stop");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    OneShotBehaviour init_message = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                ACLMessage messageTemplate = new ACLMessage(INFORM);
                messageTemplate.addReceiver(new AID("Agent1@192.168.0.117:1099/JADE",AID.ISGUID));
                messageTemplate.setContent("start");
                send(messageTemplate);

/*
             while (true){
                int x = tag.getSmoothenedLocation(10).x;
                int y = tag.getSmoothenedLocation(10).y;
                float yaw = (float) Math.toDegrees(tag.yaw);
                System.out.println("x= "+x+"+ y= +"+y+"+init yaw= "+yaw);
                  // System.out.println("init x "+x+" init y"+y);
                  Thread.sleep(1000);
                }

                */


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    TickerBehaviour tck = new TickerBehaviour(this, 1000) {
        @Override
        protected void onTick() {
            try {
                System.out.println("Location Chek");
                int x = tag.getSmoothenedLocation(10).x;
                int y = tag.getSmoothenedLocation(10).y;



                    System.out.println("x: " + x);
                    System.out.println("y: " + y);

                    float yaw = (float) Math.toDegrees(tag.yaw);
                    // yaw = (float) (yaw-301.5);
                    System.out.println("yaw mission="+yaw);


            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    CyclicBehaviour message_recieve = new CyclicBehaviour() {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println(msg);

                try {
                    removeBehaviour(mission);
                    addBehaviour(stop);

                    String[] parsed_msg = msg.getContent().split(";");

                    mission_type = parsed_msg[0];
                    path = Arrays.asList(parsed_msg[1].split(",")).stream().map(String::trim).mapToInt(Integer::parseInt).toArray(); //new int[]{4800, 4800};
                    path_iterator = 0;

                    System.out.println(Arrays.toString(path));

                    //Added
                   // mission.addSubBehaviour(obstacle_detection);
                  //  mission.addSubBehaviour(sendRot);

                    //Added

                    mission.addSubBehaviour(move);
                    mission.addSubBehaviour(stop);
                    mission.addSubBehaviour(wait);
                    mission.addSubBehaviour(message_send);

                    addBehaviour(mission);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            block();
        }
    };







    //Behaviour move = new Behaviour() {

    Behaviour move = new Behaviour() {
        @Override
        public void action() {
            try {
                int x = tag.getSmoothenedLocation(10).x;
                int y = tag.getSmoothenedLocation(10).y;

                if (x != 0 && y != 0) {
                    int target_x = path[path_iterator];
                    int target_y = path[path_iterator + 1];

                    System.out.println("x: " + x);
                    System.out.println("y: " + y);

                    float yaw = (float) Math.toDegrees(tag.yaw);
                   // yaw = (float) (yaw-301.5);
                    System.out.println("yaw mission="+yaw);

                    double diff_y = target_y - y;
                    double diff_x = target_x - x;

                    System.out.println("diff_y="+diff_y);
                    System.out.println("diff_x="+diff_x);


                   double dist = Point2D.distance(x, y, target_x, target_y);

                                       //double atan2 =Math.atan2(diff_y, diff_x);

                    //System.out.println("atan2="+atan2);

                    //float target_angle = (float) Math.toDegrees(atan2); //??


                    float target_angle = (float) Math.toDegrees(Math.atan2(y - target_y, target_x - x)); //??


                    System.out.println("Target Angle"+target_angle);

                    float diff_angle = target_angle - yaw;
                    System.out.println("Diff Angle"+diff_angle);



                     diff_angle = diff_angle % 360;
                     while (diff_angle < 0) { //pretty sure this comparison is valid for doubles and floats
                     diff_angle += 360.0;
                     }

                    System.out.println("- AFTER Diff Angle**"+diff_angle);


                    if (Math.abs(target_x - x) < 100 && Math.abs(target_y - y) < 100) {    // old params diff_angle > 10 && diff_angle <= 180, diff_angle < 350 && diff_angle > 180
                        path_iterator += 2;
                    } else if (diff_angle > 10 && diff_angle <= 180) {
                        Device.setSpeed(200);
                        addBehaviour(turn_right);
                        System.out.println("RIGHT");
                    } else if (diff_angle < 350 && diff_angle > 180) {
                        Device.setSpeed(200);
                        addBehaviour(turn_left);
                        System.out.println("LEFT");
                    }
//                    else if (obstacle) {
//                        addBehaviour(stop);
//                    }
                    else {
                        Device.fuzzy_speed_distance(dist,yaw); // fuzzy_speed
                       addBehaviour(go_forward);
                        System.out.println("FORWARD");
                    }



                  //  block(5000);
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        @Override
        public boolean done() {
            return path_iterator >= path.length;
        }
    };

    Behaviour wait = new Behaviour() {
        Boolean waiting = true;

        @Override
        public void action() {
            block(delay);
            waiting = false;
        }

        @Override
        public boolean done() {
            return !waiting;
        }
    };

    OneShotBehaviour message_send = new OneShotBehaviour() {
        @Override
        public void action() {
            if (mission_type.equals("pickup")){
                in_transit = true;
            }

            addBehaviour(sendRot);

            ACLMessage messageTemplate = new ACLMessage(INFORM);
            messageTemplate.addReceiver(new AID("Agent1@192.168.0.117:1099/JADE", AID.ISGUID));
            messageTemplate.setContent(mission_type + " Completed");
            send(messageTemplate);
        }
    };

    SequentialBehaviour mission = new SequentialBehaviour();

    public BadGirl() throws MqttException {
    }

    protected void setup() {






        addBehaviour(obstacle_check);
        addBehaviour(init_message);
     //   addBehaviour(lowBattery);
        addBehaviour(message_recieve);

      //  addBehaviour(tck);
    //    addBehaviour(turn_left);
    }
}