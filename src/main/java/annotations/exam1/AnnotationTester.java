package annotations.exam1;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class AnnotationTester {
    public static void main(String[] args)
            throws IOException, URISyntaxException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        System.out.println(AnnotationTester.class.getPackageName());
//        Initializer.getAllClasses(AnnotationTester.class, "app");
        Initializer.initialize(AnnotationTester.class, "app");
    }

}
