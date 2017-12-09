package lab3.Behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lab3.KebabAgent;

@SuppressWarnings("serial")
public class KebabCyclicBehaviour extends CyclicBehaviour
{
	private final KebabAgent agent;
	
	
	public KebabCyclicBehaviour(KebabAgent agent) 
	{
		this.agent = agent;
	}
	
	@Override
	public void action() 
	{
		ACLMessage aclMessage = agent.receive();
		
		if(aclMessage != null)
		{
			AID sender = aclMessage.getSender();
			ACLMessage aclMessage2 = new ACLMessage(ACLMessage.INFORM);
			aclMessage2.addReceiver(sender);
			
			if(agent.getKebabs() == 0)
			{
				System.out.println("List of kebabs is empty");
				agent.SendMessageToAgents();
				agent.doDelete();
			}
			else
			{
				aclMessage2.setContent("PleaseHereYourKebab");
				agent.setKebabs(agent.getKebabs() - 1);
			}
			agent.send(aclMessage2);
		}
		else
		{
			block();
		}
	}
}
