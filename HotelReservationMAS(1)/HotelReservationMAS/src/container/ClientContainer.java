package container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class ClientContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.CONTAINER_NAME, "ClientContainer");
            AgentContainer container = runtime.createAgentContainer(profile);
            container.createNewAgent("ClientAgent", "agents.ClientAgent", null).start();
            System.out.println("Conteneur Client démarré.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
