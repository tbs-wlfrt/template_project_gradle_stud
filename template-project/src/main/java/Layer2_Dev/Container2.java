package Layer2_Dev;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class Container2 {
    public static void main(String[] args) {


        try {
            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "false");
            Profile profile = new ProfileImpl(properties);
            AgentContainer agentContainer=runtime.createMainContainer(profile);

        Container2.start();

            AgentController buildAgent=agentContainer.createNewAgent("BuildAgent",
                    "Layer2_Dev.BuildAgent",new Object[]{});

            AgentController pushagent=agentContainer.createNewAgent("PushAgent",
                    "Layer2_Dev.PushAgent",new Object[]{});
            AgentController sortAgents=agentContainer.createNewAgent("SortAgent2",
                    "Layer2_Dev.SortAgent2",new Object[]{});
           buildAgent.start();
           pushagent.start();
            sortAgents.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void start() {
        InitComp2.initComponents();

    }

}
