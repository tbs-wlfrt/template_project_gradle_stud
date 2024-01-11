package OurCode.Connexion;

import OurCode.Devices.Device;
import OurCode.Helpers.LocationMessageParser;
import OurCode.Helpers.WarehouseBlueprint;
import OurCode.UWB.helpers.Point2D;
import OurCode.UWB.pozyx.TagIdMqtt;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;

import jade.lang.acl.ACLMessage;
import org.eclipse.paho.client.mqttv3.MqttException;

import static jade.lang.acl.ACLMessage.INFORM;

import java.util.*;


public class Agent1 extends Agent {
    String RobotAgentAPI = "RobotAgent@192.168.0.158:1099/JADE";

    // OUR ROBOT TAG ID
    static String tagID = "685C";
    static TagIdMqtt myRobotTag;
    double robotDistanceThreshold = 40;
    static {
        try {
            myRobotTag = new TagIdMqtt(tagID);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    // OTHER GROUPS ROBOT TAG ID
    static String otherRobotTagID = "975C";
    static TagIdMqtt otherRobotTag;
    static {
        try {
            otherRobotTag = new TagIdMqtt(otherRobotTagID);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * TIP: Check WarehouseBlueprint to see how the warehouse is represented
     * a stack of pair of work items in the format:
     * (crateNode, dropOffNode)
     */
    private static final Queue<Set<Integer>> workItems = new LinkedList<>();
    static {
        workItems.add(Set.of(1, 2));
        workItems.add(Set.of(3, 4));
        workItems.add(Set.of(5, 6));
        workItems.add(Set.of(7, 1));
    }
    // initialize warehouse blueprint
    private static final WarehouseBlueprint warehouseBlueprint = new WarehouseBlueprint();

    // list of current obstacles in the format: Set<Set<Integer>> linksToAvoid
    private static final Set<Set<Integer>> linksToAvoid = Set.of();

    // initial node of robot is 1
    private static int departureNode = 1;
    private static int destinationNode = 1;
    private static int crateNode = 1;
    private static int dropOffNode = 1;
    String robotPath;
    List<String> nodesAndPath;

    @Override
    protected void setup() {
        System.out.println("local name" + getAID().getLocalName());
        System.out.println("GloBal name" + getAID().getName());
        addBehaviour(message_recieve);
    }
    TickerBehaviour message_recieve = new TickerBehaviour(this, 1000) {
        public void onTick() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Message recieved at agent1:" + msg.getContent());
                String content = msg.getContent();

                if (content.equals("CHARGE")) {
                    System.out.println("Robot has low battery, calculating path to recharge point...");
                    // given current node, calculate the path to the recharge node(which is 1)
                    departureNode = dropOffNode;
                    nodesAndPath = warehouseBlueprint.calcNextPath(departureNode, 1, linksToAvoid);
                    destinationNode = 1;
                    robotPath = nodesAndPath.get(1);

                    dropOffNode = 1;
                    send_message(robotPath);
                } else if (content.equals("DROPOFF")) {
                    System.out.println("Robot picked up crate, calculating path to drop off point...");
                    // given current node, calculate the path to the drop off node
                    departureNode = crateNode;
                    nodesAndPath = warehouseBlueprint.calcNextPath(departureNode, dropOffNode, linksToAvoid);
                    destinationNode = dropOffNode;
                    robotPath = nodesAndPath.get(1);
                    send_message(robotPath);
                } else if (content.equals("TRIAGE")) {
                    System.out.println("Robot detected rotting crate, calculating path to triage point...");
                } else if (content.equals("NEXT_CRATE")) {
                    System.out.println("Robot dropped off crate, calculating path to next crate...");
                    departureNode = dropOffNode;

                    // pop the first work item from the stack
                    Set<Integer> workItem = workItems.poll();
                    // remember: workItem = (crateNode, dropOffNode)
                    crateNode = (int) Objects.requireNonNull(workItem).toArray()[0];
                    dropOffNode = (int) workItem.toArray()[1];
                    // given current node, calculate the path to the crate node
                    nodesAndPath = warehouseBlueprint.calcNextPath(departureNode, crateNode, linksToAvoid);
                    destinationNode = crateNode;
                    robotPath = nodesAndPath.get(1);
                    send_message(robotPath);
                } else if (content.startsWith("OBSTACLE")) {
                    boolean isOtherRobot = false;
                    // calculate distance of our robot and other robot
                    try {
                        double dist = myRobotTag.getSmoothenedLocation(10).dist(otherRobotTag.getSmoothenedLocation(10));
                        if(dist < robotDistanceThreshold) {
                            isOtherRobot = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!isOtherRobot) {
                        avoidObstacle(content);
                    }
                } else {
                    System.out.println("Recieved unexpected message from robot: " + msg.getContent());
                }
            }
        }
    };
    private void send_message(String content){
        ACLMessage msg = new ACLMessage(INFORM);
        msg.addReceiver(new AID(RobotAgentAPI, AID.ISGUID));
        msg.setContent(content);
        send(msg);
    }

    private void avoidObstacle(String content){
        // parse the message after "OBSTACLE", this is comma separated string of the form: OBSTACLE,PATH
        String remainingPath = content.substring(9);
        // Format of remaining path is of the form "LLRRF"
        // Combine nodeAndPath alternatingly so we get a string of the form "1R2L3R4F"
        String combinedOriginalPath = "";
        for (int i = 0; i < nodesAndPath.get(0).length(); i++) {
            combinedOriginalPath += nodesAndPath.get(0).charAt(i);
            combinedOriginalPath += nodesAndPath.get(1).charAt(i);
        }
        // loop through original path and keep poppy from original path and only pop from remaining path if it matches
        // while remaining path is not empty

        int problemNode = 0;
        while(!remainingPath.isEmpty()) {
            // check if only 1 char in remaining path
            if (remainingPath.length() == 1) {
                // Store the 2nd last character of the combined original path
                problemNode = Integer.parseInt(combinedOriginalPath.substring(combinedOriginalPath.length() - 2, combinedOriginalPath.length() - 1));
            }
            // remove last character of remaining path
            remainingPath = remainingPath.substring(0, remainingPath.length() - 1);
            // remove last 2 characters of combined original path
            combinedOriginalPath = combinedOriginalPath.substring(0, combinedOriginalPath.length() - 2);
        }
        // set the departure node to the character before the last character of the combined original path
        departureNode = Integer.parseInt(combinedOriginalPath.substring(combinedOriginalPath.length() - 2, combinedOriginalPath.length() - 1));

        // add the obstacle to the links to avoid
        linksToAvoid.add(Set.of(departureNode, problemNode));

        // calculate the path to the destination node without traversing the obstacle
        nodesAndPath = warehouseBlueprint.calcNextPath(departureNode, destinationNode, linksToAvoid);
        robotPath = nodesAndPath.get(1);
        send_message(robotPath);
        System.out.println("Robot detected obstacle, checking if other robot...");
    }


}

