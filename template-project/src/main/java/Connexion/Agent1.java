package Connexion;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import static jade.lang.acl.ACLMessage.INFORM;

public class Agent1 extends Agent {
    @Override
    protected void setup() {
        System.out.println("local name"+getAID().getLocalName());
        System.out.println("GloBal name"+getAID().getName());



/*addBehaviour(new TickerBehaviour(this, 10000) {
    @Override
    protected void onTick() {
        System.out.println("Tick");
        ACLMessage messageTemplate = new ACLMessage(INFORM);
        messageTemplate.addReceiver(new AID("Agent2@192.168.0.105",AID.ISGUID));
        messageTemplate.setContent("Test true");
        send(messageTemplate);




    }


});
*/
    }


    @Override
    protected void takeDown() {
    }

}