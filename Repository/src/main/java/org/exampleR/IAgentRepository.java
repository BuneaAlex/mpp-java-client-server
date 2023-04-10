package org.exampleR;

import org.exampleM.Agent;

public interface IAgentRepository extends IRepository<Integer, Agent>{

    Agent findAgentByLoginInformation(String agencyName,String userName,String password);
}
