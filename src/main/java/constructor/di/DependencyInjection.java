package constructor.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DependencyInjection {

    public static void main(String[] args) {

    }

    private static <T> T createInstanceRecursively(Class<T> clazz)
        throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // 생성자 정보 가져옴
        Constructor<?> constructor = getConstructor(clazz);

        // 인스턴스 담을 리스트
        List<Object> constructorArguments = new ArrayList<>();

        // parameterType을 탐색해서 생성자 재귀 호출 이 때 반환값은 인스턴스임으로 이걸 argumentValue에 저장
        for (Class<?> argumentType : constructor.getParameterTypes()) {
            Object argumentValue = createInstanceRecursively(argumentType);
            constructorArguments.add(argumentValue);
        }
        constructor.setAccessible(true);
        // 하위 클래스에서 생성된 인스턴스를 파라미터로 현재 생성자 호출
        return (T) constructor.newInstance(constructorArguments.toArray());
    }

    private static Constructor<?> getConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("No constructor for class" + clazz.getSimpleName());
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == fields.length) {
                return constructor;
            }
        }
        throw new RuntimeException("Not Found Constructor");
    }

}
