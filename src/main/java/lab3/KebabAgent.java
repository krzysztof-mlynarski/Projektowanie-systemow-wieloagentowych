package lab3;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lab3.Behaviour.KebabCyclicBehaviour;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class KebabAgent extends Agent
{
	private int kebabs = 250;
	
	@Override
	protected void setup() 
	{	
		DFServiceHelper.Register(this, "kebab", "JADE-kebab");
        System.out.println(this.getLocalName() + " register.");
        		
		addBehaviour(new KebabCyclicBehaviour(this));		
	}
		
	public void SendMessageToAgents(String message, String dfServiceType)
	{	
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType(dfServiceType);
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.addServices(serviceDescription);
		
		try 
		{
			DFAgentDescription[] result = DFService.search(this, dfAgentDescription);
			if(result.length == 0)
			{
				doDelete();
			}
			else
			{
				for (int i = 0; i < result.length; ++i) 
				{				
					ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
					aclMessage.setContent(message);
					aclMessage.addReceiver(result[i].getName());
					send(aclMessage);
				}	
			}	
		}
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}
	}

	public int getKebabs() {
		return kebabs;
	}

	public void setKebabs(int kebabs) {
		this.kebabs = kebabs;
	}
}
