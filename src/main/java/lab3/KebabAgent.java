package lab3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class KebabAgent extends Agent
{
	private int kebabs = 250;
	
	protected void setup() 
	{	
		DFServiceHelper.Register(this, "kebab", "JADE-kebab");
		
		doWait(4000);
		
		addBehaviour(new CyclicBehaviour() 
		{
			@Override
			public void action() 
			{
				ACLMessage aclMessage = receive();
				
				if(aclMessage != null)
				{
					AID sender = aclMessage.getSender();
					ACLMessage aclMessage2 = new ACLMessage(ACLMessage.INFORM);
					aclMessage2.addReceiver(sender);
					
					if(aclMessage.getContent().equals("TakeKebabs"))
					{
						if(kebabs == 0)
						{
							System.out.println("List of kebabs is empty");
							SendMessageToAgents("EMPTY", "philosopher");
							SendMessageToAgents("EMPTY", "fork");
						}
						else
						{
							aclMessage2.setContent("PleaseHereYourKebabing");
							kebabs--;
						}
						send(aclMessage2);
					}
					else
					{
						kebabs = Integer.parseInt(aclMessage.getContent());
						System.out.println("Table fill with " + kebabs + "kebabs.");
						SendMessageToAgents("START", "philosopher");
						SendMessageToAgents("START", "fork");
					}
				}
			}
		});
	}
		
	private void SendMessageToAgents(String message, String dfServiceType)
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
}
