package annotations.exam2.app;

import annotations.exam2.Initializer2.InitializeMethod;
import annotations.exam2.RetryOperation;
import java.io.IOException;

public class DatabaseConnection {

    private int failCounter = 3;

    @RetryOperation(
            numberOfRetries = 10,
            retryException = IOException.class,
            durationBetweenRetries = 1000,
            failureMessage = "Connection to database 1 failed after retries"
    )
    @InitializeMethod
    public void connectToDatabase1() throws IOException {
        System.out.println("Connecting to database 1");
        if (failCounter > 0) {
            failCounter--;
            throw new IOException("Connection failed");
        }
        System.out.println("Connection to database 1 succeeded");
    }

    @InitializeMethod
    public void connectToDatabase2() {
        System.out.println("Connection to database 2 succeeded");
    }
}
