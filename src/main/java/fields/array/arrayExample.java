package fields.array;

import java.lang.reflect.Array;
import java.util.Arrays;

public class arrayExample {

    public static void main(String[] args) {
        int[] array = {1, 2};
        double[][] doubleArray = {{1.5, 2.5}, {2.5, 3.5}};
        System.out.println(Arrays.deepToString(doubleArray));
        inspectArrayValue(doubleArray);
    }

    public static void inspectArrayValue(Object arrayObject) {
        int arrayLength = Array.getLength(arrayObject);

        System.out.print("[");
        for (int i = 0; i < arrayLength; ++i) {
            Object element = Array.get(arrayObject, i);

            if (element.getClass().isArray()) {
                inspectArrayValue(element);
            } else {
                System.out.print(element);
            }

            if (i != arrayLength - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }

    public static void inspectArrayObject(Object arrayObject) {
        Class<?> clazz = arrayObject.getClass();

        System.out.println(String.format("Is Array : %s", clazz.isArray()));

        Class<?> arrayComponentType = clazz.getComponentType();

        System.out.println(
            String.format("This is an array of type : %s", arrayComponentType.getTypeName()));

    }

}
