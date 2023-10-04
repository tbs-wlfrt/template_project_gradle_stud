package Agents;

import Layer1_Dev.dev1;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lejos.utility.Delay;

import static jade.lang.acl.ACLMessage.CONFIRM;


public class SortAgent extends Agent {
    @Override
    public void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage Message = receive();
             if (Message!=null) {
                 System.out.println(Message.getContent());
                 try {
                     Thread.sleep(5000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
              //   System.out.println(Message.getContent());
                 Delay.msDelay(3000);
                    if (dev1.waitBrick()==2) {
                          dev1.FirstStartconvoyor2();
                        Delay.msDelay(1);
                        Stopmotors();
                       }
                    else if  (dev1.waitBrick()==3) {
                        dev1.SecondStartconvoyor2();
                        Delay.msDelay(1);
                        Stopmotors();
                    }
                    else if (dev1.waitBrick()==4) {
                        dev1.ThirdStartconvoyor2();
                        Delay.msDelay(1);
                        Stopmotors();

                    }
                   else if (dev1.waitBrick()==5) {
                        dev1.FourthStartconvoyor2();
                        Delay.msDelay(1);
                        Stopmotors();
                      //  Brickstuck();
                   }
           else {

                       Brickstuck();
                    }
                   }
             else {
                      block();
                    }
            }
        });

    }
    public  void Stopmotors() {
        ACLMessage messageTemplate = new ACLMessage(CONFIRM);
        messageTemplate.addReceiver(new AID("ShredAgent", AID.ISLOCALNAME));
        messageTemplate.setContent("Brick received");
        send(messageTemplate);
        dev1.stoptshrederMotor();
         Delay.msDelay(1);
        dev1.stopconvoyor1();
    }
 public void Brickstuck() {
        ACLMessage messageTemplate = new ACLMessage(CONFIRM);
        messageTemplate.addReceiver(new AID("ShredAgent", AID.ISLOCALNAME));
        messageTemplate.setContent("Brick is stuck");
        send(messageTemplate);
        dev1.stoptshrederMotor();
        Delay.msDelay(1);
        dev1.stopconvoyor1();
    }
}
