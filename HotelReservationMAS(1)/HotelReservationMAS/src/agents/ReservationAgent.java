package agents;

import behaviours.ReservationBehaviour;
import jade.core.Agent;

public class ReservationAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("[Reservation] Agent démarré.");
        addBehaviour(new ReservationBehaviour(this));
    }
}
