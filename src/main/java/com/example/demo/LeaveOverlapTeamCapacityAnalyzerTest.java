package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@SpringBootTest
@Listeners(TestResultListener.class)
public class LeaveOverlapTeamCapacityAnalyzerTest {

    @Test
    public void dummyTest() {
        System.out.println("TestNG test running");
    }
}
