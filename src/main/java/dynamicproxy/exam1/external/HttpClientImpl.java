package dynamicproxy.exam1.external;

public class HttpClientImpl implements HttpClient {
    @Override
    public void initialize() {
        System.out.println("Initializing HTTP client");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendRequest(String request) {
        System.out.println(String.format("Sending request %s", request));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Receive response");
        return "someResponse data";
    }
}
