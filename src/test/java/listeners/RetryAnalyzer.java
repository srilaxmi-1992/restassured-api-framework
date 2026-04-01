package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RetryAnalyzer implements IRetryAnalyzer {

    private final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private ThreadLocal<Integer> retryCount = ThreadLocal.withInitial(() -> 0);
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount.get() < MAX_RETRY) {
            retryCount.set(retryCount.get() + 1);
            logger.info("Retrying the test method "+result.getMethod().getMethodName());
            return true;
        }
        retryCount.set(0);
        return false;
    }
}
