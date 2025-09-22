package com.langchain4j.demo.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

import java.util.List;

/**
 * @author dawei
 */
public interface EveningPlannerAgent {


     @Agent
     List<EveningPlan> plan(@V("mood") String mood);

}
