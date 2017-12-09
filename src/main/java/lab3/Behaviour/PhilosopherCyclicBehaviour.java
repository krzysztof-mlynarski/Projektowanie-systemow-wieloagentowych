package lab3.Behaviour;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lab3.KebabAgent;
import lab3.PhilosopherAgent;

@SuppressWarnings("serial")
public class PhilosopherCyclicBehaviour extends CyclicBehaviour
{
	private final PhilosopherAgent agent;
	
	public PhilosopherCyclicBehaviour(PhilosopherAgent agent) 
	{
		this.agent = agent;
	}

	@Override
	public void action() 
	{
		if(agent.getIsInitialized())
		{
			ACLMessage aclMessage = agent.receive();
			if(aclMessage != null)
			{
				String message = aclMessage.getContent();
				if(message.equals("PleaseHereYourKebab"))
				{
					agent.setEatenKebabs(agent.getEatenKebabs() + 1);
					System.out.println(agent.getLocalName() + " will now eat " + agent.getEatenKebabs() + " kebab.");
				}
				else if(message.equals("PickedPositive"))
				{
					if(aclMessage.getSender().getLocalName().equals(agent.getTmpLeftFork().getLocalName()))
					{
						agent.setLeftForkPickUp(true);
						agent.setLeftForkResponse(true);
					}
					if(aclMessage.getSender().getLocalName().equals(agent.getTmpRightFork().getLocalName()))
					{
						agent.setRightForkPickUp(true);
						agent.setRightForkResponse(true);
					}
				}
				else if(message.equals("PickedNegative"))
				{
					if(aclMessage.getSender().getLocalName().equals(agent.getTmpLeftFork().getLocalName()))
					{
						agent.setLeftForkPickUp(false);
						agent.setLeftForkResponse(true);
					}
					if(aclMessage.getSender().getLocalName().equals(agent.getTmpRightFork().getLocalName()))
					{
						agent.setRightForkPickUp(false);
						agent.setRightForkResponse(true);
					}
				}
				else if(message.equals("EMPTY"))
				{
					agent.doDelete();
				}
				
				agent.doWait(agent.getReactionTime());
				
				if(agent.getLeftForkResponse() && agent.getRightForkResponse())
				{
					if(!agent.getLeftForkPickUp() || !agent.getRightForkPickUp())
					{
						System.out.println(agent.getLocalName() + " requesting to put down both forks.");
						agent.PutForks();
					}
					else if (agent.getLeftForkPickUp() && agent.getRightForkPickUp())
					{
						System.out.println(agent.getLocalName() + " has both positive forks. Trying to pick kebab.");
						ACLMessage aclMessage2 = new ACLMessage(ACLMessage.REQUEST);
						aclMessage2.setContent("TakeKebabs");
						
						ServiceDescription serviceDescription = new ServiceDescription();
						serviceDescription.setType("kebab");
						DFAgentDescription dfAgentDescription = new DFAgentDescription();
						dfAgentDescription.addServices(serviceDescription);
						
						try 
						{
							DFAgentDescription[] result = DFService.search(agent, dfAgentDescription);
							if(result.length == 0)
							{
								agent.doDelete();
							}
							else
							{
								for (int i = 0; i < result.length; ++i) 
								{
									aclMessage2.addReceiver(result[i].getName());
									agent.send(aclMessage2);
								}	
							}							
						} 
						catch (FIPAException e) 
						{
							e.printStackTrace();
						}
						
						agent.PutForks();
					}
				}
			}
			else
			{
				block();
			}
		}
		else
		{
			if(KebabAgent.getForks() != null && !KebabAgent.getForks().isEmpty())
			{
				int forksCount = KebabAgent.getForks().size();
				
				for (AID fork : KebabAgent.getForks()) 
				{
					if (agent.getId() == Integer.parseInt(fork.getLocalName().substring(4))) 
					{
						agent.setTmpLeftFork(fork);
					} 
					else 
					{
						if (agent.getId() == 0) 
						{
							if ((forksCount - 1) == Integer.parseInt(fork.getLocalName().substring(4))) 
							{
								agent.setTmpRightFork(fork);
							}
						}
						else 
						{
							if ((agent.getId() - 1) == Integer.parseInt(fork.getLocalName().substring(4))) 
							{
								agent.setTmpRightFork(fork);
							}
						}
					}
				}
			}
		}
	}
}
