package behaviours;

import agents.ReservationAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ReservationBehaviour extends CyclicBehaviour {
    private AID clientAID;
    private AID[] hotels = new AID[0];
    private final Map<AID, Integer> proposals = new HashMap<>();
    private boolean negotiationInProgress = false;

    public ReservationBehaviour(ReservationAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate requestTemplate = MessageTemplate.and(
                MessageTemplate.MatchConversationId(Constants.CONVERSATION_ID),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        ACLMessage request = myAgent.receive(requestTemplate);

        if (request != null) {
            if (negotiationInProgress) {
                System.out.println("[Reservation] Une négociation est déjà en cours. Demande ignorée.");
                return;
            }

            clientAID = request.getSender();
            proposals.clear();
            hotels = searchHotels();

            if (hotels.length == 0) {
                sendClientInfo("Aucun hôtel n'est disponible dans le DF.");
                return;
            }

            negotiationInProgress = true;
            System.out.println("[Reservation] Hôtels trouvés : " + hotels.length);
            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
            for (AID hotel : hotels) {
                cfp.addReceiver(hotel);
            }
            cfp.setConversationId(Constants.CONVERSATION_ID);
            cfp.setContent(request.getContent());
            myAgent.send(cfp);
            System.out.println("[Reservation] CFP envoyé aux hôtels.");
            return;
        }

        // Réception asynchrone des propositions des hôtels.
        MessageTemplate proposalTemplate = MessageTemplate.and(
                MessageTemplate.MatchConversationId(Constants.CONVERSATION_ID),
                MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)
        );
        ACLMessage proposal = myAgent.receive(proposalTemplate);

        if (proposal != null) {
            if (!negotiationInProgress) {
                return;
            }

            try {
                int price = Integer.parseInt(proposal.getContent().trim());
                proposals.put(proposal.getSender(), price);
                System.out.println("[Reservation] Proposition reçue de "
                        + proposal.getSender().getLocalName() + " : " + price + " DH");

                if (proposals.size() >= hotels.length) {
                    chooseOffer();
                }
            } catch (NumberFormatException e) {
                System.err.println("[Reservation] Prix invalide reçu de "
                        + proposal.getSender().getLocalName());
            }
            return;
        }

        // Réception de la confirmation envoyée par l'hôtel retenu.
        // On utilise un template simple pour INFORM afin de ne pas bloquer les autres messages.
        ACLMessage inform = myAgent.receive(MessageTemplate.and(
                MessageTemplate.MatchConversationId(Constants.CONVERSATION_ID),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)
        ));

        if (inform != null) {
            System.out.println("[Reservation] Confirmation reçue de l'hôtel.");
            sendClientInfo(inform.getContent());
            negotiationInProgress = false;
            return;
        }

        block();
    }

    private AID[] searchHotels() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription service = new ServiceDescription();
        service.setType(Constants.SERVICE_TYPE);
        template.addServices(service);

        try {
            DFAgentDescription[] results = DFService.search(myAgent, template);
            AID[] found = new AID[results.length];
            for (int i = 0; i < results.length; i++) {
                found[i] = results[i].getName();
            }
            return found;
        } catch (FIPAException e) {
            System.err.println("[Reservation] Erreur recherche DF : " + e.getMessage());
            return new AID[0];
        }
    }

    private void chooseOffer() {
        AID winner = null;
        int bestPrice = Integer.MAX_VALUE;

        for (Map.Entry<AID, Integer> entry : proposals.entrySet()) {
            if (entry.getValue() < bestPrice) {
                bestPrice = entry.getValue();
                winner = entry.getKey();
            }
        }

        if (winner == null) {
            sendClientInfo("Aucune proposition valide reçue.");
            negotiationInProgress = false;
            return;
        }

        System.out.println("[Reservation] Offre retenue : "
                + winner.getLocalName() + " à " + bestPrice + " DH");

        for (AID hotel : hotels) {
            ACLMessage decision = new ACLMessage(
                    hotel.equals(winner) ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL
            );
            decision.addReceiver(hotel);
            decision.setConversationId(Constants.CONVERSATION_ID);
            decision.setContent(hotel.equals(winner) ? "Votre offre est acceptée." : "Votre offre est refusée.");
            myAgent.send(decision);
        }
    }

    private void sendClientInfo(String content) {
        if (clientAID == null) {
            return;
        }
        ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
        inform.addReceiver(clientAID);
        inform.setConversationId(Constants.CONVERSATION_ID);
        inform.setContent(content);
        myAgent.send(inform);
    }
}
