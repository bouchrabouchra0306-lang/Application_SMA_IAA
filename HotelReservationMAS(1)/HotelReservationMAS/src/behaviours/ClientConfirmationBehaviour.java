package behaviours;

import agents.ClientAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import utils.Constants;

public class ClientConfirmationBehaviour extends CyclicBehaviour {
    public ClientConfirmationBehaviour(ClientAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchConversationId(Constants.CONVERSATION_ID),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)
        );

        ACLMessage message = myAgent.receive(template);
        if (message != null) {
            System.out.println("\n========== CONFIRMATION CLIENT ==========");
            System.out.println(message.getContent());
            System.out.println("=========================================\n");
        } else {
            block();
        }
    }
}
