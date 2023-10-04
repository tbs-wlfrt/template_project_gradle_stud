package Connexion;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Agent2 extends Agent {
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
    }
    @Override
    protected void takeDown() {
    }
}
