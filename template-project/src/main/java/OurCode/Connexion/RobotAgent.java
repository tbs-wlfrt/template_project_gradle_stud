package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.ColorPIDController;
import OurCode.Helpers.ColorPIDController_copy;
import OurCode.Helpers.LocationMessageParser;
import OurCode.Helpers.PIDController;
import OurCode.UWB.helpers.Point2D;
import UWB.mqtt.TagMqtt;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.works.debugger.events.DBEventLocation;
import org.eclipse.paho.client.mqttv3.MqttException;
import OurCode.UWB.pozyx.TagIdMqtt;


import java.util.concurrent.TimeUnit;

import static jade.lang.acl.ACLMessage.INFORM;

public class RobotAgent extends Agent {
    int[] path = new int[]{}; // a list containing the path the agent has to take
    int pathIterator = 0; // the current route within the path the agent is taking
    int delay = 5000; // the delay between each step the agent takes
    boolean obstacleExists = false; // whether the agent is an obstacle or not
    int obstacleDistanceThreshold = 25; // the maximum acceptable distance required between the agent and the obstacle
    boolean onMission = false;
    float steerCorrection = 0.07f;//0.08f;

    ColorPIDController_copy colorPID = new ColorPIDController_copy(50);
    PIDController distancePID = new PIDController(50);
    int speedMultiplier = 40;
    int motorsFullSpeed = 150;

    //float orientation;

    int junctionColor = 5;      //red


    Boolean atJunction = false;
    Boolean atCharging = false;
    Boolean obstacleDetected = false;
    float obstacleDist = 15;
    float northOrientation;

    String jadeApi = "Agent1@192.168.0.158:1099/JADE";
    //String jadeApi = "Agent1";
    // Robot of other group
    String subscriberAPI = "ControlCenterAgent@192.168.0.159:1099/JADE";


    String currentPath = ""; // the current path the agent is taking
    String desire = "crate";

    static String tagID = "685C";
    static TagIdMqtt tag;
    static {
        try {
            tag = new TagIdMqtt(tagID);
            System.out.println("GOT HERE");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    static int ultrasonic_front = 0;

    protected void setup() {

        addBehaviour(initialize);
        addBehaviour(message_recieve); // ticker

        //addBehaviour(init_message);

        addBehaviour(askNextPath); // ask for initial location of crate
        addBehaviour(follow_line_routine);

//        addBehaviour(tck); //

        //addBehaviour(follow_line_routine_right);

        //addBehaviour(follow_line_routine);
        //addBehaviour(check_junction);
        /*
        SequentialBehaviour go = new SequentialBehaviour();
        addBehaviour(go);
        go.addSubBehaviour(follow_line_routine);
         */

        //addBehaviour(obstacle_check);
        //addBehaviour(init_message);
        //   addBehaviour(lowBattery);
        //addBehaviour(message_recieve);

        //  addBehaviour(tck);
        //    addBehaviour(turn_left);
    }

    void sendMessage(String message){
        /*
        For the messaging:

        1)ACLMessage messageTemplate = new ACLMessage(INFORM);
        2)messageTemplate.addReceiver(new AID("AgentRobot@192.168.0.176:1099/JADE",AID.ISGUID));
        3)messageTemplate.setContent("Main Sends Message ->");
        4)send(messageTemplate);
        */

        // Send rot message
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID(jadeApi,AID.ISGUID));
        //messageTemplate.addReceiver(new AID(jadeApi,AID.ISLOCALNAME));
        messageTemplate.setContent(message);
        send(messageTemplate);
    }

    void sendLocationToSubscribers(){
        /*
        For the messaging:

        1)ACLMessage messageTemplate = new ACLMessage(INFORM);
        2)messageTemplate.addReceiver(new AID("AgentRobot@192.168.0.176:1099/JADE",AID.ISGUID));
        3)messageTemplate.setContent("Main Sends Message ->");
        4)send(messageTemplate);
        */

        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID(subscriberAPI,AID.ISGUID));
        //messageTemplate.addReceiver(new AID(jadeApi,AID.ISLOCALNAME));


        // send message to subscribers
        Point2D loc = tag.getLocation();
        //orientation = tag.getOrientation();
        String info = LocationMessageParser.info_to_string(loc.x, loc.y, tag.getOrientation());
        sendMessage(info);

        messageTemplate.setContent(info);
        send(messageTemplate);
    }

    void orientNorth(){
        System.out.println("Orienting robot North");
        float targetOri = northOrientation;
        float tagOri = tag.getOrientation();

        //starting to turn in fastest direction
        if(tagOri > Math.PI){
            Device.startTurnLeft(motorsFullSpeed/4);
        }
        else{
            Device.startTurnRight(motorsFullSpeed/4);
        }

        boolean oriCheck = true;
        // given targetOri and tagOri; keep turning until facing north
        while(oriCheck){
            Device.sync();
            oriCheck = !(Math.abs(tagOri - targetOri) < 0.17);     //threshold of error for turning (within 10 degrees)
            tagOri = tag.getOrientation();
        }

        Device.stop();
    }

    void rotateOnLine(){
        float startOri = tag.getOrientation();
        //turning 190 degrees
        Device.startTurnLeft(motorsFullSpeed/4);
        float targetOri = (startOri + (float)Math.PI) + (steerCorrection*2) % (2*(float)Math.PI);

        if(targetOri < 0){
            targetOri = targetOri + (float)Math.PI * 2;
        }

        float tagOri = tag.getOrientation();
        System.out.println("Start Orientation: " +startOri);
        System.out.println("target Orientation: " +targetOri);

        boolean oriCheck = true;
        Device.sync(400);

        // given tagOri and targetOri; keep turning until 180 degrees has been turned
        while(oriCheck){         //threshold of error for turning (within 10 degrees)
            Device.sync();
            oriCheck = !(Math.abs(tagOri - targetOri) < 0.17);
            tagOri = tag.getOrientation();
        }

        Device.stop();

    }

    OneShotBehaviour initialize = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Initialising robot, recording North orientation");
            northOrientation = tag.getOrientation();
        }
    };

    OneShotBehaviour askNextPath = new OneShotBehaviour() {
        @Override
        public void action() {
            if(desire.equals("crate")){
                System.out.println("ASKING FOR CRATE PATH");
                sendMessage("NEXT_CRATE");
            }
            else if(desire.equals("dropoff")){
                System.out.println("ASKING FOR DROPOFF PATH");
                sendMessage("DROPOFF");
            }

        }
    };



    //every 1s sends the location info to the central monitor
    TickerBehaviour tck = new TickerBehaviour(this, 1000) {
        @Override
        protected void onTick() {
            try {
                //TimeUnit.SECONDS.sleep(1);
                //System.out.println("tag.getLocation() = " + tag.getLocation());
                //System.out.println("tag.getOrientation() = " + tag.getOrientation());
                System.out.println("Sampled colour: " + Device.sampleBackColor());
                Point2D loc = tag.getLocation();
                //orientation = tag.getOrientation();
                String info = LocationMessageParser.info_to_string(loc.x, loc.y, tag.getOrientation());
                sendMessage(info);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };



    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        // receives message from our controller
        // sends location to subscribers
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at RobotAgent: " + msg.getContent());
                //logic for reacting to messages
                // Robot only recves paths

                //if a path is received before the current path is finished, it is because it is rerouting, so rotate 180 degrees before following the path
                rotateOnLine();

                //any time a new path is recieved, make sure robot is pointing north before starting
                currentPath = msg.getContent();
            }

        }
    };

    CyclicBehaviour pid_follower_routine = new CyclicBehaviour() {
        @Override
        public void action() {
            //TODO: modify and test so that motorspeeds arent changed, but motorsFullSpeed is (so that line follower PID works at the same time).
            Delay.msDelay(100);
            int distance = Device.sampleFrontDistance();
            //System.out.println("Speed: " + distance);

            distancePID.updateVals(distance);
            int speed = (int) Math.min(distancePID.recalibrate()*speedMultiplier, 1000);
            Device.setMotorSpeeds(speed, speed);
            Device.startMoveForward();
        }
    };


    OneShotBehaviour follow_line_routine = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Starting behaviour: follow_line_routine");
            Delay.msDelay(100);
            //PID controlled line following behaviour
            try {
                // detect if robot is at a junction
                atJunction = Device.sampleBackColor() == junctionColor;

                float frontDist = Device.sampleFrontDistance();
                System.out.println("Front dist:" + frontDist);
                obstacleDetected = 0 < frontDist && frontDist < obstacleDist;

                if (currentPath.isEmpty()){
                    Device.stop();
                }
                //atCharging = (Device.sampleBackColor() == chargingColor1) || (Device.sampleBackColor() == chargingColor2);
                else if (obstacleDetected){
                    Device.stop();
                }

                else if((!atJunction) && (!atCharging)){
                    //get light sensor reading from device
                    float sample = Device.sampleLightIntensity();
                    Device.sync(20);
                    //controller.updateVal(sample);
                    int[] motorSpeedsReduction = colorPID.recalibrate(sample);
                    //System.out.println("\nGOT: " + sample + "" + "\nMotorspeeds:" + motorSpeedsReduction[0] + ", " + motorSpeedsReduction[1]);
                    //set device motor speeds to new values calculated by PID controller
                    Device.setMotorSpeeds(motorsFullSpeed-motorSpeedsReduction[0], motorsFullSpeed-motorSpeedsReduction[1]);
                    Device.startMoveForward();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int onEnd() {
            if (obstacleDetected){
                sendMessage("OBSTACLE,"+currentPath);
                addBehaviour(avoid_obstacle);
            }
            else if ((!atJunction) && (!atCharging))
                addBehaviour(follow_line_routine);
            else
                addBehaviour(rotate_to_exit);
            return super.onEnd();
        }

    };



    //behaviour used when the robot is currently at a junction (node) and needs to turn to its exit
    OneShotBehaviour rotate_to_exit = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Starting behaviour: rotate_to_exit");
            System.out.println("atJunction: " + atJunction);
            System.out.println("atCharging: " + atCharging);
            System.out.println("detected Colour: " + Device.sampleBackColor());
            Device.stop();
            try {
                tag = new TagIdMqtt(tagID);
                System.out.println("GOT HERE");
            } catch (MqttException e) {
                e.printStackTrace();
            }
            block(100);
            // turn device based on the current path
            float lightIntensity;
            float startOri = tag.getOrientation();
            float targetOri = 0;
            if(currentPath.isEmpty()){
                currentPath = "X";
                orientNorth();
                if(desire.equals("crate")){
                    desire = "dropoff";
                }
                else if(desire.equals("dropoff")){
                    desire = "crate";
                }
            }
            switch (currentPath.charAt(0)){
                case 'L':
                    System.out.println("Turning Left");
                    Device.startTurnLeft(motorsFullSpeed/4);
                    targetOri = (startOri - ((float)Math.PI/2)) - (steerCorrection*10);

                    break;
                case 'R':
                    System.out.println("Turning Right");
                    Device.startTurnRight(motorsFullSpeed/4);
                    targetOri = (startOri + ((float)Math.PI/2) - steerCorrection) % (2*(float)Math.PI);
                    break;
                case 'B':
                    System.out.println("Turning 180 degrees");
                    Device.startTurnLeft(motorsFullSpeed/4);
                    targetOri = (startOri + (float)Math.PI) + (steerCorrection*2) % (2*(float)Math.PI);
                    break;
                default:
                    break;
            }

            if(!(currentPath.charAt(0) == 'F')){
                if(targetOri < 0){
                    targetOri = targetOri + (float)Math.PI * 2;
                }

                float tagOri = tag.getOrientation();
                System.out.println("Start Orientation: " +startOri);
                System.out.println("target Orientation: " +targetOri);

                boolean colCheck = true;
                boolean oriCheck = true;

                Device.sync(400);

                // given startOriDeg and tagOriDeg; keep turning until 90 degrees has been turned
                while(oriCheck && colCheck){         //threshold of error for turning (within 10 degrees)
                    Device.sync();
                    oriCheck = !(Math.abs(tagOri - targetOri) < 0.17);
                    lightIntensity = Device.sampleLightIntensity();
                    colCheck = !(colorPID.fullBlack <= lightIntensity && lightIntensity < (colorPID.setPoint+5));

                    tagOri = tag.getOrientation();
                    System.out.println("light intensity:" + lightIntensity);
                }
                System.out.println("colCheck: " + colCheck + "    oriCheck: " + oriCheck);

                //if stopping due to edge detection, keep rotating a little further to stop on middle of line
                if ((!colCheck) && (currentPath.charAt(0) == 'L')){
                    Device.sync(40);
                }


            }

            Device.stop();

            // reset the values for the colorPID
            colorPID.resetValsOnTurn();
        }

        public int onEnd() {
            // pop first character of next path
            currentPath = currentPath.substring(1);

            // check if current path is empty, if so send a message to ask for NEXT_PATH
//            if (currentPath.length() == 0){
//                addBehaviour(askNextCrate); // ask for next location of crate
//            }

            atJunction = false;
            atCharging = false;
            // TODO: RE ADD THIS CHRISTOPHER NOLAN
            if(!currentPath.isEmpty()){
                addBehaviour(go_forward);
            }
            addBehaviour(follow_line_routine);
            //TODO: insert section that makes sure we dont think we are at a different junction becasue we havent moved
            return super.onEnd();
        }
    };


    /*
        BEHAVIOUR DEFINITIONS
     */



    //behaviours for robot movement - too fine grained?
    OneShotBehaviour go_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.setMotorSpeeds(motorsFullSpeed, motorsFullSpeed);
                Device.moveForward(850);
                System.out.println("Forward");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    OneShotBehaviour avoid_obstacle = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                System.out.println("Avoiding obstacle");
                String response = "";
                ACLMessage msg;

                // sleep for 1 second
                block(1000);

                // try receiving message otherwise add avoid obstacle behaviour
                try {
                    msg = receive();
                    response = msg.getContent();
                    if(response.equals("WAIT")){
                        Device.sync(50);
                        addBehaviour(follow_line_routine);
                    }
                    else{
                        currentPath = response;
                        //follow new path
                        addBehaviour(rotate_to_exit);
                    }
                } catch (Exception e) {
                    addBehaviour(avoid_obstacle);
                    // step out of this behaviour
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int onEnd() {
            obstacleDetected = false;
            return super.onEnd();
        }
    };



/*


    OneShotBehaviour init_message = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                ACLMessage messageTemplate = new ACLMessage(INFORM);
                messageTemplate.addReceiver(new AID("Agent1@192.168.0.117:1099/JADE", AID.ISGUID));
                messageTemplate.setContent("start");
                send(messageTemplate);

             while (true){
                int x = tag.getSmoothenedLocation(10).x;
                int y = tag.getSmoothenedLocation(10).y;
                float yaw = (float) Math.toDegrees(tag.yaw);
                System.out.println("x= "+x+"+ y= +"+y+"+init yaw= "+yaw);
                  // System.out.println("init x "+x+" init y"+y);
                  Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
                */

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

}