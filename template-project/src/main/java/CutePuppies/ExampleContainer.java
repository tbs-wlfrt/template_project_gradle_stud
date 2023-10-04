package CutePuppies;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class ExampleContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();

            String target = "192.168.0.117";
            String source = "192.168.0.165";
            ProfileImpl p = new ProfileImpl(target, 1099, null, false);

            p.setParameter(Profile.LOCAL_HOST, source);
            p.setParameter(Profile.LOCAL_PORT,"1099");

            AgentContainer agentContainer = runtime.createAgentContainer(p);
            ExampleContainer.start();

            System.out.println("badgirl");
            AgentController badGirl = agentContainer.createNewAgent("BadGirl", "CutePuppies.BadGirl", new Object[]{});
            //AgentController goodBoy = agentContainer.createNewAgent("Good Boy", "Dogs.BadGirl", new Object[]{});
            System.out.println("trying to start");
            badGirl.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        Device.init();
    }
}
