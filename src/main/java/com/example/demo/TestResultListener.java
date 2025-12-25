package com.example.demo;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

public class TestResultListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Tests Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Tests Finished: " + context.getName());
    }
}
