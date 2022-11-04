package fields.json;

public class JsonTest {

    public static void main(String[] args) throws IllegalAccessException {
        Address address = new Address(321, "seoul");
        A a = new A(13, "hello", address);
        String json = JsonWriter.objectToJson(a);
        System.out.println(json);
    }
}
