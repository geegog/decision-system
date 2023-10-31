package com.inbank.decisionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDecisionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(DecisionSystemApplication::main).with(TestDecisionSystemApplication.class).run(args);
    }

}
