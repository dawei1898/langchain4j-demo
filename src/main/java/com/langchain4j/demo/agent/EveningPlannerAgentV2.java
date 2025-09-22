package com.langchain4j.demo.agent;

import com.langchain4j.demo.model.EveningPlan;
import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.declarative.ParallelExecutor;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.service.V;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 声明式定义智能体
 *
 * @author dawei
 */
public interface EveningPlannerAgentV2 {

    // 并行工作流
    @ParallelAgent(outputName = "plans", subAgents = {
            @SubAgent(type = MovieExpert.class, outputName = "movies"), // 电影专家
            @SubAgent(type = FoodExpert.class, outputName = "meals")    // 美食专家
    })
    List<EveningPlan> plan(@V("mood") String mood);


    // 工作流线程池
    @ParallelExecutor
    static Executor executor(){
        return Executors.newFixedThreadPool(2);
    }

    // 输出处理
    @Output
    static List<EveningPlan> createPlans(@V("movies") List<String> movies, @V("meals") List<String> meals){
        List<EveningPlan> eveningPlans = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (i >= meals.size()) {
                break;
            }
            EveningPlan eveningPlan = new EveningPlan();
            eveningPlan.setMovie(movies.get(i));
            eveningPlan.setMeal(meals.get(i));
            eveningPlans.add(eveningPlan);
        }
        return eveningPlans;
    }

}
