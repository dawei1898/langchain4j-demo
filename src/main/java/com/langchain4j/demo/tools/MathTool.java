package com.langchain4j.demo.tools;

import dev.langchain4j.agent.tool.Tool;

/**
 * 数学工具类
 *
 * @author dawei
 */
public class MathTool {

    @Tool("Sums 2 given numbers")
    double sum(double a, double b) {
        return a + b;
    }

    @Tool("Returns a square root of a given number")
    double squareRoot(double x) {
        return Math.sqrt(x);
    }
}
