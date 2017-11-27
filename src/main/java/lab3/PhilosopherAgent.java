package lab3;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import lab3.Behaviour.CreateTickerBehaviour;
import lab3.Behaviour.PhilosopherCyclicBehaviour;
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
	
	@Override
	protected void setup() 
	{
		DFServiceHelper.Register(this, "philosopher", "JADE-philosopher");
		
		setReactionTime(new Random().nextInt(max - min + 1) + min);
	
        System.out.println(this.getLocalName() + " register with " + getReactionTime() + " reaction time.");
        
		addBehaviour(new PhilosopherCyclicBehaviour(this));
	}
	
	public void Start()
	{
		addBehaviour(new CreateTickerBehaviour(this, getReactionTime()));
	}
	
	public void PutForks()	
	{
		ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);

		aclMessage.setContent("PutDown");
		aclMessage.addReceiver(getTmpLeftFork());
		aclMessage.addReceiver(getTmpRightFork());
		
		send(aclMessage);
		
		setLeftForkResponse(false);
		setLeftForkPickUp(false);
		setRightForkResponse(false);
		setRightForkPickUp(false);
	}
	
	protected void takeDown()
	{
		System.out.println("-----------------" + this.getLocalName() + "-----------------");
		System.out.println(this.getLocalName() + " eaten kebabs: " + getEatenKebabs());
		System.out.println("-------------------------------------------");
	}

	public AID getTmpLeftFork() {
		return tmpLeftFork;
	}

	public void setTmpLeftFork(AID tmpLeftFork) {
		this.tmpLeftFork = tmpLeftFork;
	}

	public AID getTmpRightFork() {
		return tmpRightFork;
	}

	public void setTmpRightFork(AID tmpRightFork) {
		this.tmpRightFork = tmpRightFork;
	}

	public int getEatenKebabs() {
		return eatenKebabs;
	}

	public void setEatenKebabs(int eatenKebabs) {
		this.eatenKebabs = eatenKebabs;
	}

	public int getReactionTime() {
		return reactionTime;
	}

	public void setReactionTime(int reactionTime) {
		this.reactionTime = reactionTime;
	}

	public Boolean getLeftForkPickUp() {
		return leftForkPickUp;
	}

	public void setLeftForkPickUp(Boolean leftForkPickUp) {
		this.leftForkPickUp = leftForkPickUp;
	}

	public Boolean getLeftForkResponse() {
		return leftForkResponse;
	}

	public void setLeftForkResponse(Boolean leftForkResponse) {
		this.leftForkResponse = leftForkResponse;
	}

	public Boolean getRightForkPickUp() {
		return rightForkPickUp;
	}

	public void setRightForkPickUp(Boolean rightForkPickUp) {
		this.rightForkPickUp = rightForkPickUp;
	}

	public Boolean getRightForkResponse() {
		return rightForkResponse;
	}

	public void setRightForkResponse(Boolean rightForkResponse) {
		this.rightForkResponse = rightForkResponse;
	}
}
