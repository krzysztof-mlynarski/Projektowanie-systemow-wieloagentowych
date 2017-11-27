package lab3.Behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lab3.ForkAgent;

@SuppressWarnings("serial")
public class ForkCyclicBehaviour extends CyclicBehaviour 
{
	private ForkAgent agent;
	
	public ForkCyclicBehaviour(ForkAgent agent) 
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
			String message = aclMessage.getContent();
			
			if(message.equals("PickUp"))
			{
				ACLMessage aclMessage2 = new ACLMessage(ACLMessage.INFORM);
				aclMessage2.addReceiver(sender);
				
				if(agent.getForkPick() == false)
				{
					agent.setForkPick(true);
					aclMessage2.setContent("PickedPositive");
				}
				else
				{
					aclMessage2.setContent("PickedNegative");
				}
				agent.send(aclMessage2);
			}
			else if(message.equals("PutDown"))
			{
				if(agent.getForkPick() == true)
				{
					agent.setForkPick(false);
				}
			}
//			else if(message.equals("EMPTY"))
//			{
//				doDelete();
//			}
		}
		else
		{
			block();
		}
	}

}
