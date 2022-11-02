package constructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {


        Person person = createInstanceWithArguments(Person.class, "John", 20);
        System.out.println(person);
    }

    public static <T> T createInstanceWithArguments(Class<T> clazz, Object ...args)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == args.length) {
                return (T)constructor.newInstance(args);
            }
        }
        System.out.println("적절한 인수를 가진 생성자를 찾지 못했습니다");
        return null;
    }
    public static void printConstructors(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.printf("class %s has %d declared constructors%n", clazz.getSimpleName(),
            constructors.length);
        for (int i = 0; i < constructors.length; ++i) {
            Class<?>[] parameterTypes = constructors[i].getParameterTypes();

            List<String> parameterTypeNames = Arrays.stream(parameterTypes)
                .map(Class::getSimpleName)
                .collect(Collectors.toList());
            System.out.println(parameterTypeNames);

        }
    }

    static class Person {

        private final Address address;
        private final String name;
        private final int age;

        public Person() {
            this.name = "1234";
            this.age = 0;
            this.address = null;
        }

        public Person(String name) {
            this.name = name;
            this.age = 0;
            this.address = null;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        static class Address {

            private final String street;

            public Address(String street) {
                this.street = street;
            }
        }

        @Override
        public String toString() {
            return "Person{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
        }
    }
}
