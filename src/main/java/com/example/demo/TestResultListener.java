package com.example.demo;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestResultListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Optional: before test starts
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Optional: on test success
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Optional: on test failure
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Optional: when test is skipped
    }

    @Override
    public void onStart(ITestContext context) {
        // Optional: before all tests
    }

    @Override
    public void onFinish(ITestContext context) {
        // Optional: after all tests
    }
}
