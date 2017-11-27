package lab2;

import jade.core.AID;

public class Agents 
{
	private AID agentId;
	private int status;
	
	public Agents(AID agentId) {
		this.agentId = agentId;
		this.status = 0;
	}
	
	public AID getAgentId() {
		return agentId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
