package dynamicproxy.exam1.external;

public interface HttpClient {

    void initialize();

    String sendRequest(String request);
}
