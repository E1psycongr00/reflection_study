package annotations.exam1;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Initializer {

    public static void initialize(Class<?> startClass, String... packageNames)
            throws URISyntaxException, IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> classes = getAllClasses(startClass, packageNames);
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitializeMethod.class)) {
                    Object instance = clazz.getConstructor().newInstance();
                    method.invoke(instance);
                }
            }
        }
    }

    public static List<Class<?>> getAllClasses(Class<?> startClass, String... packageNames)
            throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageNames) {
            URI uri = Objects.requireNonNull(startClass.getResource(packageName)).toURI();
            Path packageFullPath = Paths.get(uri);
            List<Path> filePaths;
            try (Stream<Path> filesPath = Files.list(packageFullPath)) {
                filePaths = filesPath.collect(Collectors.toList());
            }
            classes.addAll(getClasses(startClass, packageName, filePaths));
        }
        return classes;
    }

    private static List<Class<?>> getClasses(Class<?> startClass, String packageName, List<Path> filePaths)
            throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        for (Path filePath : filePaths) {
            String fileName = filePath.getFileName().toString();
            if (fileName.endsWith(".class")) {
                String packagePath = startClass.getPackageName() + "." + packageName;
                String classFullName = packagePath + "." + fileName.replaceFirst("\\.class$", "");
                Class<?> clazz = Class.forName(classFullName);
                classes.add(clazz);
            }
        }
        return classes;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface InitializeMethod {
    }
}
