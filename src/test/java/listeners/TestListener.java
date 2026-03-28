package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, ISuiteListener {

    private int passed = 0, failed = 0, skipped = 0;
    // methods executes once when Testsuite starts
    public void onStart(ISuite suite) {
        System.out.println("\n===== SUITE: " + suite.getName() + " =====");
    }

    public void onFinish(ISuite suite) {
        System.out.println("\n===== FINAL SUMMARY =====");
        System.out.println("  Passed : " + passed);
        System.out.println("  Failed : " + failed);
        System.out.println("  Skipped: " + skipped);
        System.out.println("=========================\n");
    }

    // Methods override in ITestListener
    // Executes for every test method
    public void onTestStart(ITestResult result) {
        System.out.println("START TEST : " + result.getName());
    }

    public void onTestSuccess(ITestResult result) {
        passed++;
        System.out.println("TEST SUCCESS : " + result.getName());
    }

    public void onTestFailure(ITestResult result) {
        failed++;
        System.out.println("TEST FAIL : " + result.getName());
    }

    public void onTestSkipped(ITestResult result) {
        skipped++;
        System.out.println("TEST SKIPPED : " + result.getName());
    }
}
