package Analyzer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<String> stringClass = String.class;

        Map<String, Integer> mapObject = new HashMap<>();

        Class<?> hasMapClass = mapObject.getClass();
        Class<?> squareClass = Class.forName("Main$Square");


        printClassInfo(Collections.class, boolean.class, int[][].class, Color.class);
    }

    private static void printClassInfo(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            System.out.printf("class name : %s, class package name : %s%n",
                clazz.getSimpleName(),
                clazz.getPackage());
            Class<?>[] implementedInterfaces = clazz.getInterfaces();

            for (Class<?> implementedInterface : implementedInterfaces) {
                System.out.printf("class %s implements : %s, %n",
                    clazz.getSimpleName(),
                    implementedInterface.getSimpleName());
            }

            System.out.println("Is array " + clazz.isArray());
            System.out.println("is Primitive " + clazz.isPrimitive());
            System.out.println("is enum " + clazz.isEnum());
            System.out.println("is interface " + clazz.isInterface());
            System.out.println("is anonymous " + clazz.isAnonymousClass());

            System.out.println();
            System.out.println();
        }
    }

    private enum Color {
        BLUE,
        RED,
        GREEN
    }

    private static interface Drawable {

        int getNumberOfCorners();
    }

    private static class Square implements Drawable {

        @Override
        public int getNumberOfCorners() {
            return 4;
        }
    }

}
