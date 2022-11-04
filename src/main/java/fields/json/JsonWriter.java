package fields.json;

import java.lang.reflect.Field;

public class JsonWriter {

    public static String objectToJson(Object instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }

            stringBuilder.append(formatStringValue(field.getName()));
            stringBuilder.append(":");

            if (field.getType().isPrimitive()) {
                stringBuilder.append(formatPrimitiveValue(field, instance));
            } else if (field.getType().equals(String.class)) {
                stringBuilder.append(formatStringValue(field.get(instance).toString()));
            } else {
                stringBuilder.append(objectToJson(field.get(instance)));
            }

            if (i != fields.length -1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static String formatPrimitiveValue(Field field, Object parentInstance) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type.equals(boolean.class)
                || type.equals(int.class)
                || type.equals(long.class)
                || type.equals(short.class)) {
            return field.get(parentInstance).toString();
        }
        if (type.equals(double.class) || type.equals(float.class)) {
            return String.format("%.2f", field.get(parentInstance));
        }
        throw new RuntimeException(String.format("Type: %s %s is unsupported", type.getName()));
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }
}
