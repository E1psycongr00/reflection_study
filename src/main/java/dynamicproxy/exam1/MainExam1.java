package dynamicproxy.exam1;

import dynamicproxy.exam1.external.HttpClient;
import dynamicproxy.exam1.external.HttpClientImpl;
import dynamicproxy.exam1.external.impl.DatabaseReader;
import dynamicproxy.exam1.external.impl.DatabaseReaderImpl;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainExam1 {

    public static void main(String[] args) throws InterruptedException {
        HttpClient httpClient = new HttpClientImpl();
        DatabaseReader databaseReader = createProxy(new DatabaseReaderImpl());

        useDatabaseReader(databaseReader);
    }

    public static void userHttpClient(HttpClient httpClient) {
        httpClient.initialize();
        String response = httpClient.sendRequest("some request");
        System.out.println(String.format("Http response is : %s", response));
    }

    public static void useDatabaseReader(DatabaseReader databaseReader) throws InterruptedException {
        int rowsInGamesTable = databaseReader.countRowsInTable("GamesTable");

        System.out.println(String.format("there are %s rows in GamesTable", rowsInGamesTable));
        String[] data = databaseReader.readRow("SELECT * from GamesTable");

        System.out.println(String.format("Received %s", String.join(" , ", data)));
    }


    public static <T> T createProxy(Object originalObject) {
        Class<?>[] interfaces = originalObject.getClass().getInterfaces();

        TimeMeasuringProxyHandler timeMeasuringProxyHandler = new TimeMeasuringProxyHandler(originalObject);
        Class<?> clazz = originalObject.getClass();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces,
                timeMeasuringProxyHandler);
    }

    private static class TimeMeasuringProxyHandler implements InvocationHandler {
        private final Object originalObject;

        public TimeMeasuringProxyHandler(Object originalObject) {
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result;

            System.out.println(String.format("Measuring Proxy - Before Excuting method : %s()", method.getName()));

            long startTime = System.currentTimeMillis();
            try {
                // try catch를 해주어야 하는 이유는 발생한 예외를 프록시가 InvocationTargetException으로 감싼다.
                // 그래서 그부분을 캐치해서 예외처리 해주어야 안전하게 동작한다.
                result = method.invoke(originalObject, args);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            long endTime = System.currentTimeMillis();

            System.out.println();
            System.out.println(String.format("Measuring Proxy - Execution of %s() took %dms \n", method.getName(),
                    endTime - startTime));

            return result;
        }
    }
}
