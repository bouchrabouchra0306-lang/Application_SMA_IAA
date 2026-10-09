package agents;

import behaviours.HotelBehaviour;
import behaviours.HotelRegistrationBehaviour;
import jade.core.Agent;

public class HotelAgent extends Agent {
    private int price;

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            try {
                price = Integer.parseInt(args[0].toString());
            } catch (NumberFormatException e) {
                price = 500;
            }
        } else {
            price = 500;
        }

        System.out.println("[" + getLocalName() + "] Démarré. Prix = " + price + " DH");
        addBehaviour(new HotelRegistrationBehaviour(this));
        addBehaviour(new HotelBehaviour(this, price));
    }

    @Override
    protected void takeDown() {
        System.out.println("[" + getLocalName() + "] Arrêté.");
    }
}
