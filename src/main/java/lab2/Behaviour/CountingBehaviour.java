package lab2.Behaviour;

import java.io.IOException;
import java.util.Random;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import lab2.CountingAgent;
import lab2.MatrixFragment;

@SuppressWarnings("serial")
public class CountingBehaviour extends CyclicBehaviour
{
	private MatrixFragment matrixFragment;
	
	public CountingBehaviour(CountingAgent agent)
	{
		this.agent = agent;
	}

	@Override
	public void action()
	{
		if(agent.getStatus() == 0)
		{			
			ServiceDescription serviceDescription = new ServiceDescription();
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
						ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
						aclMessage.addReceiver(result[i].getName());
						System.out.println(agent.getLocalName() + " ready!");
						agent.send(aclMessage);
					}	
				}	
			}
			catch (FIPAException e) 
			{
				e.printStackTrace();
			}
			
			agent.setStatus(1);
		}
		else if(agent.getStatus() == 1)
		{
			ACLMessage aclMessage = agent.receive(MessageTemplate.and(MessageTemplate.MatchReplyWith(agent.getLocalName()), MessageTemplate.MatchPerformative(ACLMessage.CFP)));
			
			if(aclMessage != null)
			{
				ACLMessage aclMessage2 = aclMessage.createReply();
				agent.setStatus(0);
				
				try 
				{
					matrixFragment = (MatrixFragment) aclMessage.getContentObject();
				} 
				catch (UnreadableException e) 
				{
					e.printStackTrace();
				}
				
				if(new Random().nextInt(100) > 20)
				{
					aclMessage2.setPerformative(ACLMessage.CONFIRM);
					
					System.out.println(agent.getLocalName() + ": starting calculations.");
					
					double res = 0;
					for(int i = 0; i < matrixFragment.getSize(); i++)
					{
						res += matrixFragment.getCol()[i] * matrixFragment.getRow()[i];
					}
					
					matrixFragment.setResult(res);
					
					try 
					{
						aclMessage2.setContentObject(matrixFragment);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}				
					
					System.out.println(agent.getLocalName() + ": waiting " + agent.getDelay() + "ms");
					agent.doWait(agent.getDelay());
					
					System.out.println(agent.getLocalName() + ": send result");
					agent.send(aclMessage2);
				} 
				else 
				{
					aclMessage2.setPerformative(ACLMessage.FAILURE);
					
					System.out.println(agent.getLocalName() + ": error!");
					
					try 
					{
						aclMessage2.setContentObject(matrixFragment);
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					agent.send(aclMessage2);
					
					System.out.println(agent.getLocalName() + ": waiting 2000 ms");
					agent.doWait(agent.getDelay());
				}	
			}
		}
	}

}
