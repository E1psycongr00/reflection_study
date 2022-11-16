package annotations.exam2;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RetryOperation {
    Class<? extends Throwable>[] retryException() default {Exception.class};
    long durationBetweenRetries() default 0;
    String failureMessage() default "Operation failed after retrying";
    int numberOfRetries();
}
