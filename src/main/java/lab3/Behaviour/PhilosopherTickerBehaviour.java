package lab3.Behaviour;

import java.util.Random;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lab3.PhilosopherAgent;

@SuppressWarnings("serial")
public class PhilosopherTickerBehaviour extends TickerBehaviour
{
	private final PhilosopherAgent agent;
	
	public PhilosopherTickerBehaviour(PhilosopherAgent agent, long period) 
	{
		super(agent, period);
		this.agent = agent;
	}
	
	@Override
	protected void onTick()
	{
		if(agent.getIsInitialized())
		{
	        ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);

	        if(!agent.getLeftForkPickUp() && !agent.getRightForkPickUp())
	        {
	            int selectFork = new Random().nextInt(1);
	            if(selectFork == 0)
	            {
	            	aclMessage.addReceiver(agent.getTmpLeftFork());
	            }
	            else
	            {
	            	aclMessage.addReceiver(agent.getTmpRightFork());
	            }
	        }
	        else if (agent.getLeftForkPickUp() && !agent.getRightForkPickUp())
	        {
	        	aclMessage.addReceiver(agent.getTmpRightFork());
	        }
	        else if (agent.getRightForkPickUp() && !agent.getLeftForkPickUp())
	        {
	        	aclMessage.addReceiver(agent.getTmpLeftFork());
	        }

	        aclMessage.setContent("PickUp");
	        agent.send(aclMessage);
		}
		else
		{
			agent.PutForks();
		}
	}
}
