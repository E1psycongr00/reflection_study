package annotations.exam4.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Annotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ScanPackages {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ScheduleExecutorClass {

    }

    @Repeatable(ExecutionSchedules.class)
    @Target(ElementType.METHOD)
    public @interface ExecuteOnSchedule {

        int delaySeconds() default 0;

        int periodSeconds();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ExecutionSchedules {
        ExecuteOnSchedule[] value();
    }
}
