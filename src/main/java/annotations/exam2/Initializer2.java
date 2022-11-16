package annotations.exam2;

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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Initializer2 {

    public static void initialize(Class<?> startClass)
            throws Throwable {

        ScanPackages scanPackages = startClass.getAnnotation(ScanPackages.class);

        if (scanPackages == null || scanPackages.values().length == 0) {
            System.out.println("해당 패키지를 찾을 수 없습니다.");
            return;
        }

        List<Class<?>> classes = getAllClasses(startClass, scanPackages.values());
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitializeMethod.class)) {
                    Object instance = clazz.getConstructor().newInstance();
                    callInitializeMethod(instance, method);
                }
            }
        }
    }

    private static void callInitializeMethod(Object instance, Method method) throws Throwable {
        RetryOperation retryOperation = method.getAnnotation(RetryOperation.class);

        int numberOfRetries = retryOperation == null ? 0 : retryOperation.numberOfRetries();
        while (true) {
            try {
                method.invoke(instance);
                break;
            } catch (InvocationTargetException e){
                Throwable targetException = e.getTargetException();
                if (numberOfRetries > 0 && Set.of(retryOperation.retryException()).contains(targetException.getClass())) {
                    numberOfRetries--;
                    System.out.println("Retrying ...");
                    Thread.sleep(retryOperation.durationBetweenRetries());
                } else if(retryOperation != null) {
                    throw new Exception(retryOperation.failureMessage(), targetException);
                } else {
                    throw targetException;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
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
