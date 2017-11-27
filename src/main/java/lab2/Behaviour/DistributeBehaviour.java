package lab2.Behaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import lab2.Agents;
import lab2.DistributorAgent;
import lab2.MatrixFragment;

@SuppressWarnings("serial")
public class DistributeBehaviour extends CyclicBehaviour
{
	private DistributorAgent agent;
	
	public DistributeBehaviour(DistributorAgent agent) 
	{
		this.agent = agent;
	}
	
	@Override
	public void action() 
	{
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("counting");
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.addServices(serviceDescription);
		
		try 
		{
			DFAgentDescription[] result = DFService.search(agent, dfAgentDescription);
			List<Agents> tempAgents = new ArrayList<>();
			
			if(result.length == 0)
			{
				agent.doDelete();
			}
			else
			{
				for (int i = 0; i < result.length; i++) 
				{			
					boolean exists = false;
					if(agent.getAgents() != null)
					{
						for (Agents a : agent.getAgents())
						{
							if(result[i].getName().equals(a.getAgentId()))
							{
								exists = true;
								tempAgents.add(a);
								break;
							}
						}
					}
					
					if(!exists) 
					{
						tempAgents.add(new Agents(result[i].getName()));
					}		
				}	

				agent.setAgents(tempAgents);
				
				ACLMessage aclMessage = agent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(aclMessage != null)
				{
					for(Agents a : agent.getAgents())
					{
						if(a.getAgentId().equals(aclMessage.getSender()))
						{
							a.setStatus(1);
						}
					}
				}
			}	
		}
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}
		
		for(Agents a : agent.getAgents())
		{
			if(a.getStatus() == 1)
			{
				ACLMessage aclMessage = new ACLMessage(ACLMessage.CFP);
				aclMessage.addReceiver(a.getAgentId());
				
				try 
				{
					for(MatrixFragment matrixFragment : agent.getMatrixFragments())
					{
						if(matrixFragment.getState() == 0)
						{
							matrixFragment.setState(1);
							aclMessage.setContentObject(matrixFragment);
							aclMessage.setReplyWith(a.getAgentId().getLocalName());
							a.setStatus(0);
							
							System.out.println(agent.getLocalName() + ": sent fragment " + matrixFragment.getRowIndex() + "," + matrixFragment.getColIndex() + " to agent - " + a.getAgentId().getLocalName());
							agent.send(aclMessage);
							break;
						}
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		
		ACLMessage aclMessage = agent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
		if(aclMessage != null)
		{
			MatrixFragment matrixFragment;
			try 
			{
				matrixFragment = (MatrixFragment)aclMessage.getContentObject();

				if(matrixFragment.getState() == 2)
				{
					agent.getResultMatrix().setValue(matrixFragment.getRowIndex(), matrixFragment.getColIndex(), matrixFragment.getResult());
				}
				System.out.println(agent.getLocalName() + ": received result from agent - " + aclMessage.getSender().getLocalName());
			} 
			catch (UnreadableException e) 
			{
				e.printStackTrace();
			}
		}
		
		ACLMessage aclMessage2 = agent.receive(MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
		if(aclMessage2 != null)
		{
			try 
			{
				MatrixFragment matrixFragment = (MatrixFragment) aclMessage2.getContentObject();
	
				for(MatrixFragment matrixFragment2 : agent.getMatrixFragments())
				{
					if(matrixFragment2.getColIndex() == matrixFragment.getColIndex() && matrixFragment2.getRowIndex() == matrixFragment.getRowIndex())
					{
						matrixFragment2.setState(0);
					}
				}
				System.out.println(agent.getLocalName() + ": error from agent - "+ aclMessage2.getSender().getLocalName());
			} 
			catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
