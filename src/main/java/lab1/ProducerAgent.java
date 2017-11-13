package lab1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

@SuppressWarnings("serial")
public class ProducerAgent extends Agent 
{
	private static int current = 0;
	private Integer count = 200;
	private int interval = new Random().nextInt(150) + 15;

	private Queue<String> tokensQueue = new LinkedList<String>();
	
	protected void setup() 
	{	
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerTokens");
		serviceDescription.setName("JADE-send-token");
		
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.setName(this.getAID());
		dfAgentDescription.addServices(serviceDescription);
		
		try
		{
			DFService.register(this, dfAgentDescription);			
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}	
		
		addBehaviour(new TickerBehaviour(this, interval) 
		{
			
			@Override
			protected void onTick() 
			{
				if(current != count) 
				{
					String tok = UUID.randomUUID().toString().replaceAll("-", "");
					tokensQueue.offer(tok);
					System.out.println("Generated token: " + tok);	
					current++;
				}
			}
		});
		
		addBehaviour(new SendTokenBehaviour(this));
	}
	
	protected void takeDown()
	{
		try 
		{
			DFService.deregister(this);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
	public int getCurrent() 
	{
		return current;
	}

	public void setCurrent(int current) 
	{
		ProducerAgent.current = current;
	}
	
	public Integer getCount() 
	{
		return count;
	}
	
	public Queue<String> getTokensQueue() 
	{
		return tokensQueue;
	}	
}
