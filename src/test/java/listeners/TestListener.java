package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, ISuiteListener {

    private final Logger suiteLogger = LogManager.getLogger("SuiteLog");
    private int passed = 0, failed = 0, skipped = 0;

    @Override
    public void onStart(ISuite suite) {
        ThreadContext.put("testName", "suite-startup");  // Prevent null path on suite start
        ThreadContext.put("threadId", "0");
        suiteLogger.info("\n===== SUITE: " + suite.getName() + " =====");
    }

    @Override
    public void onFinish(ISuite suite) {
        ThreadContext.put("testName", "suite-startup");  // Restore for suite summary logging
        suiteLogger.info("\n===== FINAL SUMMARY =====");
        suiteLogger.info("  Passed : " + passed);
        suiteLogger.info("  Failed : " + failed);
        suiteLogger.info("  Skipped: " + skipped);
        suiteLogger.info("=========================\n");
        ThreadContext.clearAll();  // Clean up after suite finishes
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Only log to suite — @BeforeMethod in test class sets ThreadContext with timestamp
        suiteLogger.info("Thread-{} Starting: {}",
                Thread.currentThread().getId(),
                result.getName());
        suiteLogger.info("START TEST : " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passed++;
        // ThreadContext is still valid here — @AfterMethod clears it after this
        Logger testLogger = LogManager.getLogger(result.getTestClass().getRealClass());
        testLogger.info("TEST SUCCESS : " + Thread.currentThread().getName() + " : " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed++;
        Logger testLogger = LogManager.getLogger(result.getTestClass().getRealClass());
        testLogger.error("TEST FAIL : " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skipped++;
        Logger testLogger = LogManager.getLogger(result.getTestClass().getRealClass());
        testLogger.warn("TEST SKIPPED : " + result.getName());
    }
}