package annotations.exam4;

import annotations.exam4.annotations.Annotation.ExecuteOnSchedule;
import annotations.exam4.annotations.Annotation.ScanPackages;
import annotations.exam4.annotations.Annotation.ScheduleExecutorClass;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ScanPackages({"loaders"})
public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException, ClassNotFoundException {
        schedule();
    }

    public static void schedule() throws URISyntaxException, IOException, ClassNotFoundException {
        ScanPackages scanPackages = Main.class.getAnnotation(ScanPackages.class);
        if (scanPackages == null || scanPackages.value().length == 0) {
            return;
        }

        List<Class<?>> allClasses = getAllClasses(scanPackages.value());
        List<Method> scheduledExecutorMethods = getScheduleExecutorMethods(allClasses);

        for (Method method : scheduledExecutorMethods) {
            scheduleMethodExecution(method);
        }
    }

    private static List<Class<?>> getAllClasses(String[] packageNames)
            throws URISyntaxException, IOException, ClassNotFoundException {
        String MainPackageName = Main.class.getPackageName();
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageNames) {
            URI uri = Main.class.getResource(packageName).toURI();
            Path dir = Paths.get(uri);
            findClasses(MainPackageName, classes, dir, packageName);
        }
        return classes;
    }

    private static void findClasses(String MainPackageName, List<Class<?>> classes, Path dir, String packageName)
            throws ClassNotFoundException, IOException {
        List<Path> files;
        try (Stream<Path> list = Files.list(dir)) {
            files = list.collect(Collectors.toList());
        }
        for (Path path : files) {
            String fileName = path.getFileName().toString();
            if (!fileName.endsWith(".class")) {
                continue;
            }
            fileName = fileName.replaceFirst(".class$", "");
            String packageClassName = MainPackageName + "." + packageName + "." + fileName;
            Class<?> clazz = Class.forName(packageClassName);
            classes.add(clazz);
        }
    }

    private static void scheduleMethodExecution(Method method) {
        ExecuteOnSchedule[] schedules = method.getAnnotationsByType(ExecuteOnSchedule.class);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        for (ExecuteOnSchedule schedule : schedules) {
            scheduledExecutorService.scheduleAtFixedRate(
                    () -> runWhenScheduled(method),
                    schedule.delaySeconds(),
                    schedule.periodSeconds(),
                    TimeUnit.SECONDS
            );
        }
    }

    private static void runWhenScheduled(Method method) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        System.out.printf("Executing at %s ", dateFormat.format(currentDate));

        try {
            method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static List<Method> getScheduleExecutorMethods(List<Class<?>> allClasses) {
        List<Method> scheduleMethods = new ArrayList<>();
        for (Class<?> clazz : allClasses) {
            if (!clazz.isAnnotationPresent(ScheduleExecutorClass.class)) {
                continue;
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getAnnotationsByType(ExecuteOnSchedule.class).length != 0) {
                    scheduleMethods.add(method);
                }
            }
        }
        return scheduleMethods;
    }
}
