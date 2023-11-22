package OurCode.Connexion;

import OurCode.Devices.Device;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class TestContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();

            String source = "192.168.0.111";
            String target = "192.168.0.158";
            ProfileImpl p = new ProfileImpl(target, 1099, null, false);

            p.setParameter(Profile.LOCAL_HOST, source);
            p.setParameter(Profile.LOCAL_PORT,"1099");

            AgentContainer agentContainer = runtime.createAgentContainer(p);
            TestContainer.start();

            System.out.println("RobotAgent");
            AgentController robotAgent = agentContainer.createNewAgent("RobotAgent", "OurCode.Connexion.RobotAgent", new Object[]{});

            System.out.println("Starting...");
            robotAgent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        Device.init();
    }

}
