package lab1;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class SendTokenBehaviour extends CyclicBehaviour
{
	private final ProducerAgent agent;
	
	public SendTokenBehaviour(ProducerAgent agent) 
	{
		this.agent = agent;
	}

	@Override
	public void action() 
	{
		ACLMessage aclMessage = agent.receive();
		if(aclMessage != null)
		{
			ACLMessage message = aclMessage.createReply();
			
			if(!agent.getTokensQueue().isEmpty())
			{
				message.setPerformative(ACLMessage.CONFIRM);
				message.setContent(agent.getTokensQueue().poll());
			}
			else
			{
				if (agent.getCurrent() == agent.getCount())
				{
					message.setPerformative(ACLMessage.FAILURE);
					message.setContent("no-tokens");
					System.out.println(agent.getLocalName() + ": list of tokens is empty");
					System.out.println();
					agent.doDelete();
				}
				else
				{
					message.setPerformative(ACLMessage.INFORM);
					message.setContent("temp-no-tokens");
					System.out.println(agent.getLocalName() + ": temporary no tokens");
				}
			}								
			agent.send(message);
		}
		else
		{
			block();
		}
	}	
}
