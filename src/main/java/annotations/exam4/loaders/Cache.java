package annotations.exam4.loaders;

import annotations.exam4.annotations.Annotation.ExecuteOnSchedule;
import annotations.exam4.annotations.Annotation.ScheduleExecutorClass;

@ScheduleExecutorClass
public class Cache {

    @ExecuteOnSchedule(periodSeconds = 5)
    @ExecuteOnSchedule(delaySeconds = 10, periodSeconds = 1)
    public static void reloadCache() {
        System.out.println("Reloading cache");
    }
}
