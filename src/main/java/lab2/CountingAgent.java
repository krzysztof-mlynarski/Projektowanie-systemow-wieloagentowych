package lab2;

import java.util.Random;

import jade.core.Agent;
import lab2.Behaviour.CountingBehaviour;
import lab3.Helpers.DFServiceHelper;

@SuppressWarnings("serial")
public class CountingAgent extends Agent
{
	private int delay;
	private int status; //0 - busy, 1 - ready
		
	@Override
	protected void setup()
	{
		DFServiceHelper.Register(this, "counting", "counting-matrix");
		setDelay(new Random().nextInt(1000) + 500);
		addBehaviour(new CountingBehaviour(this));
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
