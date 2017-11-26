package lab3;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class PhilosopherAgent extends Agent 
{
	private AID tmpLeftFork;
	private AID tmpRightFork;
	
	private int eatenKebabs = 0;
	private int min = 500;
	private int max = 1500;
	private int reactionTime;
	
	private Boolean leftForkPickUp = false;
	private Boolean leftForkResponse = false;
	private Boolean rightForkPickUp = false;
	private Boolean rightForkResponse = false;
	
	protected void setup() 
	{
		reactionTime = new Random().nextInt(max - min + 1) + min;
		
		DFServiceHelper.Register(this, "philosopher", "JADE-philosopher");
				
		addBehaviour(new CyclicBehaviour() 
		{		
			@Override
			public void action() 
			{
				ACLMessage aclMessage = receive();
				if(aclMessage != null)
				{
					String message = aclMessage.getContent();
					
					if(message.equals("START"))
					{
						Start();
					}
					else if(message.equals("PleaseHereYourKebabing"))
					{
						eatenKebabs++;
						System.out.println(getLocalName() + "will now eat kebabing. ek " + eatenKebabs);
					}
					else if(message.equals("PickedPositive"))
					{
						if(aclMessage.getSender().getLocalName().equals(tmpLeftFork.getLocalName()))
						{
							leftForkPickUp = true;
							leftForkResponse = true;
						}
						if(aclMessage.getSender().getLocalName().equals(tmpRightFork.getLocalName()))
						{
							rightForkPickUp = true;
							rightForkResponse = true;
						}
					}
					else if(message.equals("PickedNegative"))
					{
						if(aclMessage.getSender().getLocalName().equals(tmpLeftFork.getLocalName()))
						{
							leftForkPickUp = false;
							leftForkResponse = true;
						}
						if(aclMessage.getSender().getLocalName().equals(tmpRightFork.getLocalName()))
						{
							rightForkPickUp = false;
							rightForkResponse = true;
						}
					}
					else if(message.equals("EMPTY"))
					{
						doDelete();
					}
					
					myAgent.doWait(reactionTime);
					
					if(leftForkResponse && rightForkResponse)
					{
						if(!leftForkPickUp || !rightForkPickUp)
						{
							System.out.println(getLocalName() + " requesting to put down both forks.");
							PutForks();
						}
						else if (leftForkPickUp && rightForkPickUp)
						{
							System.out.println(getLocalName() + " has both positive forks. Trying to pick kebabs.");
							ACLMessage aclMessage2 = new ACLMessage(ACLMessage.REQUEST);
							aclMessage2.setContent("TakeKebabs");
							
							ServiceDescription serviceDescription = new ServiceDescription();
							serviceDescription.setType("philosopher");
							DFAgentDescription dfAgentDescription = new DFAgentDescription();
							dfAgentDescription.addServices(serviceDescription);
							
							try 
							{
								DFAgentDescription[] result = DFService.search(myAgent, dfAgentDescription);
								if(result.length == 0)
								{
									doDelete();
								}
								else
								{
									for (int i = 0; i < result.length; ++i) 
									{
										aclMessage.addReceiver(result[i].getName());
										send(aclMessage);
									}	
								}							
							} 
							catch (FIPAException e) 
							{
								e.printStackTrace();
							}
							
							PutForks();
						}
					}
				}
				else
				{
					block();
				}
			}
		});
	}
	
	private void Start()
	{
		addBehaviour(new TickerBehaviour(this, reactionTime) 
		{			
			@Override
			protected void onTick() 
			{
				ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
				aclMessage.setContent("PickUp");
				aclMessage.addReceiver(tmpRightFork);
				send(aclMessage);
				
				aclMessage.removeReceiver(tmpRightFork);
				aclMessage.addReceiver(tmpLeftFork);
				send(aclMessage);
			}
		});
	}
	
	private void PutForks()
	{
		ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
		
		aclMessage.setContent("PutDown");
		aclMessage.addReceiver(tmpLeftFork);
		aclMessage.addReceiver(tmpRightFork);
		
		send(aclMessage);
		
		leftForkResponse = false;
		leftForkPickUp = false;
		rightForkResponse = false;
		rightForkPickUp = false;
	}
	
	protected void takeDown()
	{
		System.out.println("-----------------" + this.getLocalName() + "-----------------");
		System.out.println(this.getLocalName() + " eaten kebabs: " + eatenKebabs);
		System.out.println("-------------------------------------------");
	}
}
