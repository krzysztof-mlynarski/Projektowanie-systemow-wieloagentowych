package lab2.Helpers;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFServiceHelper
{
	public static void Register(Agent agent, String dfServiceType, String dfServiceName)
	{	
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(dfServiceType);
        serviceDescription.setName(dfServiceName);
        
		DFAgentDescription dfAgentDescription = new DFAgentDescription();
		dfAgentDescription.setName(agent.getAID());
        dfAgentDescription.addServices(serviceDescription);
        
        try
        {
            DFService.register(agent, dfAgentDescription);
        } 
        catch (FIPAException fe) 
        {
            fe.printStackTrace();
        }
	}
	
	public static void Deregister(Agent agent)
	{
		try
		{
            DFService.deregister(agent);
        } 
		catch (FIPAException fe) 
		{
            fe.printStackTrace();
        }
	}
}
