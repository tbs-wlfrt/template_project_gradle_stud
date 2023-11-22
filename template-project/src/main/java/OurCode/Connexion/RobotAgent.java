package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.ColorPIDController;
import OurCode.Helpers.PIDController;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.INFORM;

public class RobotAgent extends Agent {
    int[] path = new int[]{}; // a list containing the path the agent has to take
    int pathIterator = 0; // the current route within the path the agent is taking
    int delay = 5000; // the delay between each step the agent takes
    boolean obstacleExists = false; // whether the agent is an obstacle or not
    int obstacleDistanceThreshold = 25; // the maximum acceptable distance required between the agent and the obstacle
    boolean onMission = false;

    ColorPIDController colorPID = new ColorPIDController(75);
    PIDController distancePID = new PIDController(50);
    int speedMultiplier = 40;

    String mission_type = ""; // the mission the agent has to complete

    /*
    static TagMqtt robotTag;

    static {
        try {
            robotTag = new TagMqtt("6841");
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
    }


     */

    static int ultrasonic_front = 0;
    //static int ultrasonic_left = 0;
    //static int ultrasonic_right = 0;

    protected void setup() {

        //BEHAVIOURS (for our scenarios):
        //PID for following (cyclic behaviour for checking distance and updating speed)
        //follow predefined path (one shot behaviour for just following a hardcoded set of instructions)
        SequentialBehaviour go = new SequentialBehaviour();
        addBehaviour(go);
        go.addSubBehaviour(follow_line_routine);
        //addBehaviour(obstacle_check);
        //addBehaviour(init_message);
        //   addBehaviour(lowBattery);
        //addBehaviour(message_recieve);

        //  addBehaviour(tck);
        //    addBehaviour(turn_left);
    }


    CyclicBehaviour pid_follower_routine = new CyclicBehaviour() {
        @Override
        public void action() {
            Delay.msDelay(100);
            int distance = Device.sampleFrontDistance();
            System.out.println("Speed: " + distance);

            distancePID.updateVals(distance);
            int speed = (int) Math.min(distancePID.recalibrate()*speedMultiplier, 1000);
            System.out.println("speed: " + speed);

            //System.out.println("Speed: " + speed);
            Device.setMotorSpeeds(speed, speed);
            Device.startMoveForward();
        }
    };


    CyclicBehaviour follow_line_routine = new CyclicBehaviour() {
        @Override
        public void action() {
            Delay.msDelay(20);
            //PID controlled line following behaviour
            try {
                //get light sensor reading from device
                float sample = Device.sampleLightIntensity();
                Device.sync(20);
                //controller.updateVal(sample);
                int[] motorSpeeds = colorPID.recalibrate(sample);
                System.out.println("\nGOT: " + sample + "" + "\nMotorspeeds:" + motorSpeeds[0] + ", " + motorSpeeds[1]);
                //set device motor speeds to new values calculated by PID controller
                Device.setMotorSpeeds(130-motorSpeeds[0], 130-motorSpeeds[1]);
                Device.startMoveForward();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /*
        BEHAVIOUR DEFINITIONS
     */

    // cyclicly check if there's an obstacle in front of the agent
    CyclicBehaviour obstacle_detection = new CyclicBehaviour() {
        @Override
        public void action() {
            try{
                int freeDistance = Device.sampleFrontDistance();
                obstacleExists = freeDistance < obstacleDistanceThreshold;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    /*
    // cyclicly check for obstacles
    CyclicBehaviour obstacle_check = () -> {
        try{
            ultrasonic_front = Device.lookAhead_front();
            ultrasonic_left = Device.lookAhead_left();
            ultrasonic_right = Device.lookAhead_right();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    };
     */

    /*
    OneShotBehaviour sendRot = new OneShotBehaviour() {
        @Override
        public void action() {

            System.out.println("Rotten runs");
            //  while (in_transit){
            // Random number
            Random rand = new Random();
            int upperbound = 25;
            int int_random = rand.nextInt(upperbound);
            if (int_random == 10) {
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

     */

    /*
    // takes 2 params, send a message every tick
    TickerBehaviour lowBattery = new TickerBehaviour(this, 100) {
        @Override
        protected void onTick() {
            // Random number
            Random rand = new Random();
            int upperbound = 10;
            int int_random = rand.nextInt(upperbound);
            if (int_random == 0) {
                // Send a low battery message
                //   ACLMessage messageTemplate = new ACLMessage(INFORM);
                //    messageTemplate.addReceiver(new AID("R2@192.168.0.136:1099/JADE",AID.ISGUID));
                //    messageTemplate.setContent("battery");
                //     send(messageTemplate);


                //     System.out.println("Low battery");
            }
        }
    };
     */
    /*
    //behaviours for robot movement - too fine grained?
    OneShotBehaviour go_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.moveForward(500);
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
                Device.moveBackwards(500);
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
                Device.turnLeftInPlace(500);
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
                Device.turnRightInPlace(500);
                System.out.println("Left");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

     */


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
                messageTemplate.addReceiver(new AID("Agent1@192.168.0.117:1099/JADE", AID.ISGUID));
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

    /*
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
                System.out.println("yaw mission=" + yaw);


            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };
    */

    /*
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
                    pathIterator = 0;

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

     */

    /*
    Behaviour move = new Behaviour() {
        @Override
        public void action() {
            try {
                int x = robotTag.getSmoothenedLocation(10).x;
                int y = robotTag.getSmoothenedLocation(10).y;

                if (x != 0 && y != 0) {
                    int target_x = path[pathIterator];
                    int target_y = path[pathIterator + 1];

                    System.out.println("x: " + x);
                    System.out.println("y: " + y);

                    float yaw = (float) Math.toDegrees(robotTag.yaw);
                    // yaw = (float) (yaw-301.5);
                    System.out.println("yaw mission=" + yaw);

                    double diff_y = target_y - y;
                    double diff_x = target_x - x;

                    System.out.println("diff_y=" + diff_y);
                    System.out.println("diff_x=" + diff_x);


                    double dist = Point2D.distance(x, y, target_x, target_y);

                    //double atan2 =Math.atan2(diff_y, diff_x);

                    //System.out.println("atan2="+atan2);

                    //float target_angle = (float) Math.toDegrees(atan2); //??


                    float target_angle = (float) Math.toDegrees(Math.atan2(y - target_y, target_x - x)); //??


                    System.out.println("Target Angle" + target_angle);

                    float diff_angle = target_angle - yaw;
                    System.out.println("Diff Angle" + diff_angle);


                    diff_angle = diff_angle % 360;
                    while (diff_angle < 0) { //pretty sure this comparison is valid for doubles and floats
                        diff_angle += 360.0;
                    }

                    System.out.println("- AFTER Diff Angle**" + diff_angle);


                    if (Math.abs(target_x - x) < 100 && Math.abs(target_y - y) < 100) {    // old params diff_angle > 10 && diff_angle <= 180, diff_angle < 350 && diff_angle > 180
                        pathIterator += 2;
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
                        CutePuppies.Device.fuzzy_speed_distance(dist, yaw); // fuzzy_speed
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
            return pathIterator >= path.length;
        }
    };

     */

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

    /*
    OneShotBehaviour message_send = new OneShotBehaviour() {
        @Override
        public void action() {
            if (mission_type.equals("pickup")) {
                onMission = true;
            }

            addBehaviour(sendRot);

            ACLMessage messageTemplate = new ACLMessage(INFORM);
            messageTemplate.addReceiver(new AID("Agent1@192.168.0.117:1099/JADE", AID.ISGUID));
            messageTemplate.setContent(mission_type + " Completed");
            send(messageTemplate);
        }
    };

     */

    SequentialBehaviour mission = new SequentialBehaviour();


    OneShotBehaviour avoid_obstacle = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                System.out.println("Following hardcoded square avoidance path...");
                //Device.startMoveForward();
                //Delay.msDelay(2000);
                Device.stop();
                Device.pivotLeftBy(90, 500);
                Device.moveForward(2000);
                Device.pivotRightBy(90, 500);
                Device.moveForward(2000);
                Device.pivotRightBy(90, 500);
                Device.moveForward(2000);
                Device.pivotLeftBy(90, 500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    OneShotBehaviour start_moving = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                System.out.println("Boogie commence...");
                //Device.startMoveForward();
                //Delay.msDelay(2000);
                Device.stop();
                Device.startMoveForward();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

/*
    public RobotAgent() throws MqttException {
    }
*/




}
