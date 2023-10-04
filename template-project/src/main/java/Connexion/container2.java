package Connexion;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class container2 {
    public static void main(String[] args) {

        /*String[] bootOptions = new String[7];
        bootOptions[0] = "-gui";
        bootOptions[1] = "-local-port";
        bootOptions[2] = "1099";
        bootOptions[3] = "-container-name";
        bootOptions[4] = "Launch container";
        bootOptions[5] = "-agents";
        bootOptions[6] = "Agent2:Connexion.Agent2";
        jade.Boot.main(bootOptions);

*/
        try {
            Runtime runtime = Runtime.instance();
            String target ="192.168.0.105";
            String source ="192.168.0.176";
            ProfileImpl p = new ProfileImpl(target,1099,null,false);

            p.setParameter(Profile.LOCAL_HOST,source);
            p.setParameter(Profile.LOCAL_PORT,"1099");

            AgentContainer agentContainer=runtime.createAgentContainer(p);
            Connexion.container2.start();

           // Properties properties = new ExtendedProperties();
           // properties.setProperty(Profile.GUI, "true");
      //      properties.
            //Profile profile = new ProfileImpl(properties);
            AgentController agent2=agentContainer.createNewAgent("Agent2",
                    "Connexion.Agent2",new Object[]{});
            agent2.start();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void start() {

    }
}
