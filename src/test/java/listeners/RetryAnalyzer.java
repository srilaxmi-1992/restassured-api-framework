package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < MAX_RETRY) {
            retryCount++;
            logger.info("Retrying test: " + result.getName() + " | Attempt: " + retryCount);
            return true;
        }
        return false;

    }
}
