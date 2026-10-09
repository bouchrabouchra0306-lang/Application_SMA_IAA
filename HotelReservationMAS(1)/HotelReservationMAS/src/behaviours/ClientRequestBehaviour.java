package behaviours;

import agents.ClientAgent;
import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Constants;

public class ClientRequestBehaviour extends WakerBehaviour {
    public ClientRequestBehaviour(ClientAgent agent, long timeout) {
        super(agent, timeout);
    }

    @Override
    protected void onWake() {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(new AID(Constants.RESERVATION_AGENT, AID.ISLOCALNAME));
        request.setConversationId(Constants.CONVERSATION_ID);
        request.setContent("Réserver une chambre pour 2 personnes");
        myAgent.send(request);

        System.out.println("[Client] Demande de réservation envoyée à ReservationAgent.");
    }
}
