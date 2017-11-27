package lab3.Behaviour;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lab3.PhilosopherAgent;

@SuppressWarnings("serial")
public class CreateTickerBehaviour extends TickerBehaviour
{
	private final PhilosopherAgent agent;
	
	public CreateTickerBehaviour(PhilosopherAgent agent, long period) 
	{
		super(agent, period);
		this.agent = agent;
	}
	
	@Override
	protected void onTick()
	{
		ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
		aclMessage.setContent("PickUp");
		aclMessage.addReceiver(agent.getTmpRightFork());
		agent.send(aclMessage);
		
		aclMessage.removeReceiver(agent.getTmpRightFork());
		aclMessage.addReceiver(agent.getTmpLeftFork());
		agent.send(aclMessage);
	}

}
