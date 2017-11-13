package lab1;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class GetTokenBehaviour extends TickerBehaviour
{
	private final ConsumerAgent agent;
	
	public GetTokenBehaviour(ConsumerAgent agent, long period)
	{
		super(agent, period);
		this.agent = agent;
	}
	
	@Override
	protected void onTick() 
	{	
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerTokens");
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.addServices(serviceDescription);
		
		try 
		{
			DFAgentDescription[] result = DFService.search(agent, dfAgentDescription);
			if(result.length == 0)
			{
				//System.out.println("No tokens service found.");
				agent.doDelete();
			}
			else
			{
				for (int i = 0; i < result.length; ++i) 
				{
					ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
					aclMessage.addReceiver(result[0].getName());
					aclMessage.setContent(agent.getLocalName());
					agent.send(aclMessage);
				}								
			}							
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}			
	}
}
