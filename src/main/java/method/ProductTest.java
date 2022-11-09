package method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductTest {

    public static void main(String[] args) {
        testGetters(ChildProduct.class);
        testSetters(ChildProduct.class);
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        if (clazz == null || clazz.equals(Object.class)) {
            return Collections.emptyList();
        }

        Field[] currentClassFields = clazz.getDeclaredFields();

        List<Field> inheritedFields = getAllFields(clazz.getSuperclass());

        List<Field> allFields = new ArrayList<>();
        allFields.addAll(Arrays.asList(currentClassFields));
        allFields.addAll(inheritedFields);

        return allFields;
    }
    public static void testSetters(Class<?> dataClass) {
        List<Field> fields = getAllFields(dataClass);
        for (Field field : fields) {
            String setterName = "set" + capitalizeFirstLetter(field.getName());

            Method setterMethod;
            try {
                setterMethod = dataClass.getMethod(setterName, field.getType());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter: %s not found", setterName));
            }

            if (!setterMethod.getReturnType().equals(void.class)) {
                throw new IllegalStateException(
                    String.format("Setter: %s must return void type", setterName));
            }
        }
    }

    public static void testGetters(Class<?> dataClass) {
        List<Field> fields = getAllFields(dataClass);

        Map<String, Method> methodNameToMethod = mapMethodNameToMethod(dataClass);

        for (Field field : fields) {
            String getterName = "get" + capitalizeFirstLetter(field.getName());

            if (!methodNameToMethod.containsKey(getterName)) {
                throw new IllegalStateException(
                    String.format("Field : %s doesn't have getter", field.getName()));
            }

            Method getter = methodNameToMethod.get(getterName);

            if (!getter.getReturnType().equals(field.getType())) {
                throw new IllegalStateException(
                    String.format("Getter method : %s() has return type %s but expected %s",
                        getter.getName(),
                        getter.getReturnType(),
                        field.getType().getTypeName()));
            }

            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(
                    String.format("Getter : %s has arguments", getterName));
            }
        }

    }

    private static String capitalizeFirstLetter(String fieldName) {
        return fieldName.substring(0, 1)
            .toUpperCase()
            .concat(fieldName.substring(1));
    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> dataClass) {
        Method[] allMethods = dataClass.getMethods();

        Map<String, Method> nameToMethod = new HashMap<>();

        for (Method method : allMethods) {
            nameToMethod.put(method.getName(), method);
        }

        return nameToMethod;
    }
}
