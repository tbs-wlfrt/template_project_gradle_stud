package Agents;
import Layer1_Dev.dev1;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import static jade.lang.acl.ACLMessage.INFORM;

//import static Layer1_Dev.DropSensor.idcolor;

public class Init1Agent extends Agent  {
    static boolean press = false;
    @Override
    protected void setup() {

        addBehaviour(new Behaviour() {
            @Override
            public void action() {
                /** Send message to DropAgent */

                    if (dev1.ispressed()) {
                         press = true;
                    ACLMessage messageTemplate = new ACLMessage(INFORM);
                    messageTemplate.addReceiver(new AID("DropAgent", AID.ISLOCALNAME));
                    messageTemplate.setContent("Start");
                    send(messageTemplate);
                }}

            @Override
            public boolean done() {
                if (press)
                return true;
                else
                    return  false;
            }
        });

        }

    @Override
    protected void takeDown() {
    }

}


