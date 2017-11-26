package lab3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class ForkAgent extends Agent 
{
	private Boolean forkPick = false;
	
	protected void setup()
	{		
		DFServiceHelper.Register(this, "fork", "JADE-fork");
		
		addBehaviour(new CyclicBehaviour(this) 
		{		
			@Override
			public void action() 
			{
				ACLMessage aclMessage = receive();
				if(aclMessage != null)
				{
					AID sender = aclMessage.getSender();
					String message = aclMessage.getContent();
					
					if(message.equals("PickUp"))
					{
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.INFORM);
						aclMessage2.addReceiver(sender);
						
						if(forkPick == false)
						{
							forkPick = true;
							aclMessage2.setContent("PickedPositive");
						}
						else
						{
							aclMessage2.setContent("PickedNegative");
						}
						send(aclMessage2);
					}
					else if(message.equals("PutDown"))
					{
						if(forkPick == true)
						{
							forkPick = false;
						}
					}
					else if(message.equals("EMPTY"))
					{
						doDelete();
					}
				}
				else
				{
					block();
				}
			}
		});
	}
}
