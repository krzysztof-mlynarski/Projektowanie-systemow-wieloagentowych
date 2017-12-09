package lab3;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import lab3.Behaviour.KebabCyclicBehaviour;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class KebabAgent extends Agent
{
	private int kebabs = 250;
	private static List<AID> philosophers = new ArrayList<>();
	private static List<AID> forks = new ArrayList<>();
	
	@Override
	protected void setup() 
	{	
		DFServiceHelper.Register(this, "kebab", "kebab");
		doWait(200);
        System.out.println(this.getLocalName() + " ready!");
        		
		addBehaviour(new KebabCyclicBehaviour(this));		
		FindAgents();
	}
			
	private void FindAgents()
	{
		getPhilosophersAgentIds();
		getForksAgentIds();
		
		if(philosophers.size() != forks.size())
		{
			SendMessageToAgents();
			return;
		}
	}
	
	private void getPhilosophersAgentIds()
	{
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("philosopher");
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
					philosophers.add(result[i].getName());
				}	
			}
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void getForksAgentIds()
	{
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("fork");
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
					forks.add(result[i].getName());
				}	
			}
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void SendMessageToAgents()
	{
		List<AID> agents = new ArrayList<>();
		agents.addAll(philosophers);
		agents.addAll(forks);
		
		ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
		aclMessage.setContent("EMPTY");
		
		for(AID agent : agents)
		{
			aclMessage.addReceiver(agent);
		}
		
		send(aclMessage);
	}

	public int getKebabs() {
		return kebabs;
	}

	public void setKebabs(int kebabs) {
		this.kebabs = kebabs;
	}

	public static List<AID> getPhilosophers() {
		return philosophers;
	}

	public static List<AID> getForks() {
		return forks;
	}
}
