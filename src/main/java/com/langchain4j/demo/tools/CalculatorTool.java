package com.langchain4j.demo.tools;

import dev.langchain4j.agent.tool.Tool;

/**
 * 计算工具类
 *
 * @author dawei
 */
public class CalculatorTool {

    // 加法
    @Tool("Sums 2 given numbers")
    double sum(double a, double b) {
        return a + b;
    }

    // 减法
    @Tool("Subtracts 2 given numbers")
    double subtract(double a, double b) {
        return a - b;
    }

    // 乘法
    @Tool("Multiplies 2 given numbers")
    double multiply(double a, double b) {
        return a * b;
    }

    // 除法
    @Tool("Divides 2 given numbers")
    double divide(double a, double b) {
        return a / b;
    }

    // 平方根
    @Tool("Returns a square root of a given number")
    double squareRoot(double x) {
        return Math.sqrt(x);
    }
}
