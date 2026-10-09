package behaviours;

import agents.HotelAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import utils.Constants;

public class HotelRegistrationBehaviour extends OneShotBehaviour {
    public HotelRegistrationBehaviour(HotelAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        DFAgentDescription description = new DFAgentDescription();
        description.setName(myAgent.getAID());

        ServiceDescription service = new ServiceDescription();
        service.setType(Constants.SERVICE_TYPE);
        service.setName(Constants.SERVICE_NAME);
        description.addServices(service);

        try {
            DFService.register(myAgent, description);
            System.out.println("[" + myAgent.getLocalName() + "] Service publié dans le DF.");
        } catch (FIPAException e) {
            System.err.println("[" + myAgent.getLocalName() + "] Erreur publication DF : " + e.getMessage());
        }
    }
}
