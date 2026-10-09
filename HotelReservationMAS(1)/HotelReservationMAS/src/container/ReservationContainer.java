package container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class ReservationContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.CONTAINER_NAME, "ReservationContainer");
            AgentContainer container = runtime.createAgentContainer(profile);
            container.createNewAgent("ReservationAgent", "agents.ReservationAgent", null).start();
            System.out.println("Conteneur Reservation démarré.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
