package agents;

import behaviours.ClientConfirmationBehaviour;
import behaviours.ClientRequestBehaviour;
import jade.core.Agent;

public class ClientAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("[Client] Agent démarré.");
        addBehaviour(new ClientConfirmationBehaviour(this));
        // Envoie la demande après 2 secondes, pour laisser les hôtels publier leurs services.
        addBehaviour(new ClientRequestBehaviour(this, 2000));
    }
}
