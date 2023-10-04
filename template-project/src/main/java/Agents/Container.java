package Agents;


import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class Container   {
    public static void main(String[] args) {


        try {
            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "false");
            Profile profile = new ProfileImpl(properties);
            AgentContainer agentContainer=runtime.createMainContainer(profile);

        Container.start();

            AgentController initAgent=agentContainer.createNewAgent("Init1Agent",
                    "Agents.Init1Agent",new Object[]{});
            AgentController dropAgent=agentContainer.createNewAgent("DropAgent",
                    "Agents.DropAgent",new Object[]{});
            AgentController shredAgent=agentContainer.createNewAgent("ShredAgent",
                    "Agents.ShredAgent",new Object[]{});
            AgentController sortAgents=agentContainer.createNewAgent("SortAgent",
                    "Agents.SortAgent",new Object[]{});
            initAgent.start();
            dropAgent.start();
            shredAgent.start();
            sortAgents.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void start() {
      //  InitComp.initComponents(); /**Configuration of all components **/

    }

}
