package lab1;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class RequestReplyBehaviour extends CyclicBehaviour
{
	private final ConsumerAgent agent;
	private int interval;
	
	public RequestReplyBehaviour(ConsumerAgent agent, int interval)
	{
		this.agent = agent;
		this.interval = interval;
	}

	@Override
	public void action() 
	{
		ACLMessage message = agent.receive();
		
		if(message != null)
		{
			if(message.getPerformative() == ACLMessage.CONFIRM)
			{
				agent.setTokensCount(agent.getTokensCount() + 1);
				System.out.println(agent.getLocalName() + " downloaded token");
			}
			else if(message.getPerformative() == ACLMessage.FAILURE)
			{
				agent.doDelete();
			}
			
			agent.doWait(interval);
		}
		else
		{
			block();
		}
	}	
}
