package behaviours;

import agents.HotelAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Constants;

public class HotelBehaviour extends CyclicBehaviour {

    private final int price;

    public HotelBehaviour(HotelAgent agent, int price) {
        super(agent);
        this.price = price;
    }

    @Override
    public void action() {

        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchConversationId(Constants.CONVERSATION_ID),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        ACLMessage cfp = myAgent.receive(template);

        if (cfp == null) {
            block();
            return;
        }

        System.out.println(
                "[" + myAgent.getLocalName() +
                "] CFP reçu. Envoi du prix : " + price + " DH"
        );

        ACLMessage proposal = cfp.createReply();

        proposal.setPerformative(ACLMessage.PROPOSE);
        proposal.setContent(String.valueOf(price));
        proposal.setConversationId(Constants.CONVERSATION_ID);

        myAgent.send(proposal);

        // Attend la décision du ReservationAgent
        myAgent.addBehaviour(
                new HotelDecisionBehaviour(cfp.getSender())
        );
    }


    private class HotelDecisionBehaviour extends CyclicBehaviour {

        private final AID reservationAgent;

        HotelDecisionBehaviour(AID reservationAgent) {
            super(HotelBehaviour.this.getAgent());
            this.reservationAgent = reservationAgent;
        }

        @Override
        public void action() {

            MessageTemplate decisionTemplate = MessageTemplate.and(
                    MessageTemplate.and(
                            MessageTemplate.MatchConversationId(
                                    Constants.CONVERSATION_ID
                            ),
                            MessageTemplate.MatchSender(
                                    reservationAgent
                            )
                    ),
                    MessageTemplate.or(
                            MessageTemplate.MatchPerformative(
                                    ACLMessage.ACCEPT_PROPOSAL
                            ),
                            MessageTemplate.MatchPerformative(
                                    ACLMessage.REJECT_PROPOSAL
                            )
                    )
            );

            ACLMessage decision = myAgent.receive(decisionTemplate);

            if (decision == null) {
                block();
                return;
            }

            if (decision.getPerformative()
                    == ACLMessage.ACCEPT_PROPOSAL) {

                System.out.println(
                        "[" + myAgent.getLocalName() +
                        "] Offre acceptée !"
                );

                ACLMessage inform =
                        new ACLMessage(ACLMessage.INFORM);

                inform.addReceiver(reservationAgent);

                inform.setConversationId(
                        Constants.CONVERSATION_ID
                );

                inform.setContent(
                        "Réservation confirmée par "
                        + myAgent.getLocalName()
                        + " au prix de "
                        + price
                        + " DH."
                );

                myAgent.send(inform);

            } else {

                System.out.println(
                        "[" + myAgent.getLocalName() +
                        "] Offre refusée."
                );
            }

            // Le comportement a terminé sa tâche
            myAgent.removeBehaviour(this);
        }
    }
}