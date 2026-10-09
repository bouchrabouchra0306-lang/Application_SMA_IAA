package container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class HotelContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.CONTAINER_NAME, "HotelContainer");
            AgentContainer container = runtime.createAgentContainer(profile);

            container.createNewAgent("Hotel1", "agents.HotelAgent", new Object[]{"500"}).start();
            container.createNewAgent("Hotel2", "agents.HotelAgent", new Object[]{"400"}).start();
            container.createNewAgent("Hotel3", "agents.HotelAgent", new Object[]{"450"}).start();

            System.out.println("Conteneur des hôtels démarré.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
