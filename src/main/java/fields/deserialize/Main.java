package fields.deserialize;

import fields.deserialize.config.GameConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class Main {

    private static final String GAME_CONFIG_PATH = "game-properties.cfg";

    public static void main(String[] args)
        throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, URISyntaxException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        System.out.println(gameConfig);
    }

    public static <T> T createConfigObject(Class<T> clazz, String filePath)
        throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, URISyntaxException {

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource(filePath).getFile());
        BufferedReader br = new BufferedReader(new FileReader(file));

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = (T) constructor.newInstance();
        String configLine;
        while ((configLine = br.readLine()) != null) {
            System.out.println(configLine);
            String[] nameValuePair = configLine.split("=");
            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];
            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.println(
                    String.format("Property name : %s is unsupported", propertyName));
                continue;
            }
            field.setAccessible(true);
            Object parsedValue;

            if (field.getType().isArray()) {
                parsedValue = parseArray(field.getType().getComponentType(), propertyValue);
            } else {
                parsedValue = parseValue(field.getType(), propertyValue);
            }
            field.set(configInstance, parsedValue);

        }
        return configInstance;
    }

    private static Object parseArray(Class<?> arrayComponentType, String value) {
        String[] elementValues = value.split(",");
        Object arrayObject = Array.newInstance(arrayComponentType, elementValues.length);

        for (int i = 0; i < elementValues.length; i++) {
            Array.set(arrayObject, i, parseValue(arrayComponentType, elementValues[i]));
        }
        return arrayObject;
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        }
        if (type.equals(long.class)) {
            return Long.parseLong(value);
        }
        if (type.equals(double.class)) {
            return Double.parseDouble(value);
        }
        if (type.equals(float.class)) {
            return Float.parseFloat(value);
        }
        if (type.equals(String.class)) {
            return value;
        }
        throw new RuntimeException(String.format("cannot support %s property", type.getTypeName()));
    }


}
