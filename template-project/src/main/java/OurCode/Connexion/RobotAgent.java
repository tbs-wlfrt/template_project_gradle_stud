package OurCode.Connexion;

// Import the Devices our robot agent is using
import OurCode.Devices.SensorMotorDevice;
// Devices can be split in Layers (Layer1_Dev, Layer2_Dev)
// Look into PLAagent and dev1

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;

public class RobotAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("local name"+getAID().getLocalName());
        System.out.println("GloBal name"+getAID().getName());
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                //    System.out.println("asd");
                    ACLMessage Message=receive();
                    if (Message!=null) {
                        System.out.println(" Message:"+Message.getContent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        // Simple movement on a square route
        SequentialBehaviour squareRoute = new SequentialBehaviour();
        addBehaviour(squareRoute);
        squareRoute.addSubBehaviour();
    }
    @Override
    protected void takeDown() {
    }

    // Behavior definitions

}
