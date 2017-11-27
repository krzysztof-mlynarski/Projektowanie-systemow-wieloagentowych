package lab3;

import jade.core.Agent;
import lab3.Behaviour.ForkCyclicBehaviour;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class ForkAgent extends Agent 
{
	private Boolean forkPick = false;
	
	protected void setup()
	{		
		DFServiceHelper.Register(this, "fork", "JADE-fork");
		
		addBehaviour(new ForkCyclicBehaviour(this));
	}

	public Boolean getForkPick() {
		return forkPick;
	}

	public void setForkPick(Boolean forkPick) {
		this.forkPick = forkPick;
	}
}
