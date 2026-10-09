package container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainContainer {

    public static void main(String[] args) {

        Runtime runtime = Runtime.instance();

        try {

            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.GUI, "true");

            AgentContainer mainContainer =
                    runtime.createMainContainer(profile);

            System.out.println("==========================================");
            System.out.println("     HOTEL RESERVATION - SYSTEME SMA");
            System.out.println("==========================================");

            startAgent(
                    mainContainer,
                    "Hotel1",
                    "agents.HotelAgent",
                    new Object[]{"500"}
            );

            startAgent(
                    mainContainer,
                    "Hotel2",
                    "agents.HotelAgent",
                    new Object[]{"400"}
            );

            startAgent(
                    mainContainer,
                    "Hotel3",
                    "agents.HotelAgent",
                    new Object[]{"450"}
            );

            startAgent(
                    mainContainer,
                    "ReservationAgent",
                    "agents.ReservationAgent",
                    null
            );

            startAgent(
                    mainContainer,
                    "ClientAgent",
                    "agents.ClientAgent",
                    null
            );

            System.out.println("Tous les agents ont été lancés.");

        } catch (Exception e) {

            System.err.println(
                    "ERREUR : Impossible de démarrer JADE."
            );

            e.printStackTrace();

            System.err.println(
                    "Les agents ne peuvent pas être lancés."
            );
        }
    }

    private static void startAgent(
            AgentContainer container,
            String name,
            String className,
            Object[] args) throws Exception {

        AgentController agent =
                container.createNewAgent(
                        name,
                        className,
                        args
                );

        agent.start();
    }
}