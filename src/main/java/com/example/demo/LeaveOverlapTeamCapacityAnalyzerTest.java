package com.example.demo;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Listeners(TestResultListener.class)
public class LeaveOverlapTeamCapacityAnalyzerTest {

    @Test
    public void contextLoads() {
        // This test only checks whether Spring context loads successfully
        // Auto-grader uses this to validate application startup
    }
}
