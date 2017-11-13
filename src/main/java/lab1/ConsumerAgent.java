package lab1;

import java.util.Random;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;

@SuppressWarnings("serial")
public class ConsumerAgent extends Agent
{
	private int tokensCount;
	private int interval = new Random().nextInt(160) + 15;

	protected void setup()
	{	
		addBehaviour(new GetTokenBehaviour(this, interval));
		addBehaviour(new RequestReplyBehaviour(this, interval));
	}
	
	protected void takeDown()
	{
		System.out.println("-----------------" + this.getLocalName() + "-----------------");
		System.out.println(this.getLocalName() + " used " + getTokensCount() + " tokens.");
		System.out.println("-------------------------------------------");
		
		try 
		{
			DFService.deregister(this);
		}
		catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
	
	public int getTokensCount() 
	{
		return tokensCount;
	}

	public void setTokensCount(int tokensCount) 
	{
		this.tokensCount = tokensCount;
	}
}
