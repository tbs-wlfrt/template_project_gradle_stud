package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.ColorPIDController;
import OurCode.Helpers.ColorPIDController_copy;
import OurCode.Helpers.PIDController;
import UWB.mqtt.TagMqtt;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
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

    ColorPIDController_copy colorPID = new ColorPIDController_copy(50);
    PIDController distancePID = new PIDController(50);
    int speedMultiplier = 40;
    int motorsFullSpeed = 150;

    //float orientation;

    int junctionColor = 5;      //red
    Boolean atJunction = false;

    String jadeApi = "Agent1@192.168.0.158:1099/JADE";
    //String jadeApi = "Agent1";


    String mission_type = ""; // the mission the agent has to complete

    String currentPath = ""; // the current path the agent is taking


    static TagIdMqtt tag;

    static {
        try {
            tag = new TagIdMqtt("685C");
            System.out.println("GOT HERE");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    static int ultrasonic_front = 0;
    //static int ultrasonic_left = 0;
    //static int ultrasonic_right = 0;


    protected void setup() {

        addBehaviour(message_recieve);

        addBehaviour(init_message);

        addBehaviour(askNextCrate); // ask for initial location of crate
        addBehaviour(follow_line_routine);

        addBehaviour(tck);

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

    OneShotBehaviour askNextCrate = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("ASKING FOR CRATE PATH");
            sendMessage("NEXT_CRATE");
        }
    };

    TickerBehaviour tck = new TickerBehaviour(this, 1000) {
        @Override
        protected void onTick() {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("tag.getLocation() = " + tag.getLocation());
                System.out.println("tag.getOrientation() = " + tag.getOrientation());
                //orientation = tag.getOrientation();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    // oneshot behaviour that sends 1 message
    OneShotBehaviour init_message = new OneShotBehaviour() {
        @Override
        public void action() {
            ACLMessage messageTemplate = new ACLMessage(INFORM);
            messageTemplate.addReceiver(new AID(jadeApi,AID.ISGUID));
            messageTemplate.setContent("1st Message");
            send(messageTemplate);
        }
    };

    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at RobotAgent: " + msg.getContent());
                //TODO: insert logic for reacting to messages
                // Robot only recves paths
                currentPath = msg.getContent();
            }
        }
    };

    // oneshot move forward behavior
    OneShotBehaviour move_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            Device.moveForward(100);
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
                if(!atJunction){
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
            if (!atJunction)
                addBehaviour(follow_line_routine);
            else
                addBehaviour(rotate_to_exit);
            return super.onEnd();
        }

    };

    /*
    OneShotBehaviour follow_line_routine_right = new OneShotBehaviour() {
        @Override
        public void action() {
            //System.out.println("Starting behaviour: follow_line_routine");
            Delay.msDelay(100);
            //PID controlled line following behaviour
            try {
                // detect if robot is at a junction
                atJunction = Device.sampleBackColor() == junctionColor;
                if(!atJunction){
                    //System.out.println("+++++++++++++++Message sent++++++++++++++++++=");
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
            if (!atJunction)
                addBehaviour(follow_line_routine_right);
            else
                addBehaviour(rotate_right_to_exit);
            return super.onEnd();
        }
    };
    */

    //checks if the robot is currently over a junction (with either of the color sensors)
    TickerBehaviour check_junction = new TickerBehaviour(this, 100) {
        @Override
        protected void onTick() {
            /*
            steps:
            1) sample both colour sensors, check if colour is junctionColour
            2) if front sensor reads junctionColour, set both motors to same speed and go forward.
            3) if back sensor read junctionColour, stop, and set atJunction to true.
             */

            //TODO: add code for checking with front sensor.
            //behaviour for if back sensor reads junctionColor
            int sampledColor = Device.sampleBackColor();
            //System.out.println("Sampled colour: " + sampledColor);
            if (sampledColor == junctionColor){
                Device.stop();
                atJunction = true;
            }

        }
    };


    //behaviour used when the robot is currently at a junction (node) and needs to turn to its exit
    OneShotBehaviour rotate_to_exit = new OneShotBehaviour() {
        @Override
        public void action() {
            System.out.println("Starting behaviour: rotate_to_exit");
            // turn device based on the current path
            float lightIntensity = 100;
            float startOri = tag.getOrientation();
            float targetOri = 0;
            switch (currentPath.charAt(0)){
                case 'F':
                    return;
                case 'L':
                    System.out.println("Turning Left");

                    //Device.turnLeftInPlace(60); // defines the speed of the turn
                    Device.startTurnLeft(motorsFullSpeed/4);
                    targetOri = (startOri - (float)Math.PI/2);

                    break;
                case 'R':
                    System.out.println("Turning Right");
                    //Device.turnRightInPlace(60); // defines the speed of the turn
                    Device.startTurnRight(motorsFullSpeed/4);
                    targetOri = (startOri + (float)Math.PI/2) % (2*(float)Math.PI);
                    break;
            }
            if(targetOri < 0){
                targetOri = targetOri + (float)Math.PI * 2;
            }
            // check if it's between full black and set point
            /*
            while (!(colorPID.fullBlack <= lightIntensity && lightIntensity < (colorPID.setPoint+5))){
                //Device.turnLeftInPlace(100);
                Device.sync(20);
                lightIntensity = Device.sampleLightIntensity();
                //System.out.println(lightIntensity);
            }
            */
            float startOriDeg = (float) Math.toDegrees(startOri);
            float tagOriDeg = (float) Math.toDegrees(tag.getOrientation());
            float tagOri = tag.getOrientation();
            System.out.println("Start Orientation: " +startOri);
            System.out.println("target Orientation: " +targetOri);

            boolean colCheck = true;
            boolean oriCheck = true;

            Device.sync(500);
            // given startOriDeg and tagOriDeg; keep turning until 90 degrees has been turned
            while(oriCheck && colCheck){         //threshold of error for turning (within 10 degrees)
                Device.sync();
                oriCheck = !(Math.abs(tagOri - targetOri) < 0.17);
                lightIntensity = Device.sampleLightIntensity();
                colCheck = !(colorPID.fullBlack <= lightIntensity && lightIntensity < (colorPID.setPoint+5));

                tagOri = tag.getOrientation();
                System.out.println("Orientation: " +tagOri);

            }

            /*
            while(Math.abs(startOri - tagOri) < Math.PI / 2 || Math.abs(startOri - tagOri) > 3 * Math.PI / 2) {
                // Code to turn goes here
                // Update tagOri after turning
                tagOri = tag.getOrientation();
            }

            float tolerance = 0.0873f; // 5 degrees in radians

            // given startOri and tagOri; keep turning until 90 degrees (pi/2 radians) or -90 degrees (3*pi/2 radians) has been turned
            while(Math.abs(startOri - tagOri) < Math.PI / 2 - tolerance || Math.abs(startOri - tagOri) > 3 * Math.PI / 2 + tolerance) {
                // Code to turn goes here
                // Update tagOri after turning
                tagOri = tag.getOrientation();
            }
            */

            /*
            float tolerance = 0.0873f; // 5 degrees in radians

            // given startOri and tagOri; keep turning until 90 degrees (pi/2 radians) or -90 degrees (3*pi/2 radians) has been turned
            while(Math.abs(startOri - tagOri) < Math.PI / 2 - tolerance || Math.abs(startOri - tagOri) > 3 * Math.PI / 2 + tolerance) {
                // Code to turn goes here
                // Update tagOri after turning
                tagOri = tag.getOrientation();
            }

             */

            Device.stop();

            // reset the values for the colorPID
            colorPID.resetValsOnTurn();
        }

        public int onEnd() {
            // pop first character of next path
            currentPath = currentPath.substring(1);

            // check if current path is empty, if so send a message to ask for NEXT_PATH
            if (currentPath.length() == 0){
                addBehaviour(askNextCrate); // ask for next location of crate
            }

            atJunction = false;
            addBehaviour(go_forward);
            addBehaviour(follow_line_routine);
            //TODO: insert section that makes sure we dont think we are at a different junction becasue we havent moved
            return super.onEnd();
        }
    };

    /*
    //behaviour used when the robot is currently at a junction (node) and needs to turn to its exit
    OneShotBehaviour rotate_right_to_exit = new OneShotBehaviour() {
        @Override
        public void action() {
            // Keeps turning incrementally until the front sensor reads black
            //System.out.println("Starting behaviour: rotate_to_exit");
            Device.stop();
            //TODO: update so that robot turns left or right according to next desired node.
            //if next exit is left, rotate left until front sensor reads black
            float lightIntensity = 100;
            Device.turnRightInPlace(60); // defines the speed of the turn
            Device.startTurnRight(motorsFullSpeed/4);
            // check if it's between full black and set point
            while (!(colorPID.fullBlack <= lightIntensity && lightIntensity < (colorPID.setPoint+5))){
                //Device.turnLeftInPlace(100);
                Device.sync(20);
                lightIntensity = Device.sampleLightIntensity();
                //System.out.println(lightIntensity);
            }
            Device.stop();

            // rotate a little bit to left to make sure we are on the line
            Device.turnLeftInPlace(60);
            Device.startTurnRight(motorsFullSpeed/4);
            Delay.msDelay(500);
            Device.stop();
            // reset the values for the colorPID
            colorPID.resetValsOnTurn();
        }

        public int onEnd() {
            atJunction = false;
            addBehaviour(go_forward);
            addBehaviour(follow_line_routine_right);
            //TODO: insert section that makes sure we dont think we are at a different junction becasue we havent moved
            return super.onEnd();
        }
    };
    */

    /*
        BEHAVIOUR DEFINITIONS
     */

    /*
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

     */

    TickerBehaviour obstacle_detection = new TickerBehaviour(this, 200) {
        @Override
        protected void onTick() {
            try{
                int freeDistance = Device.sampleFrontDistance();
                obstacleExists = freeDistance < obstacleDistanceThreshold;
                if (obstacleExists){
                    sendMessage("OBSTACLE");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    };



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
    //behaviours for robot movement - too fine grained?
    OneShotBehaviour go_forward = new OneShotBehaviour() {
        @Override
        public void action() {
            try {
                Device.setMotorSpeeds(motorsFullSpeed, motorsFullSpeed);
                Device.moveForward(2000);
                System.out.println("Forward");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /*

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


}
