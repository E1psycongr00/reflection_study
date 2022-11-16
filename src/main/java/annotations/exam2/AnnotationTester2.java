package annotations.exam2;

@ScanPackages(values = {"app"})
public class AnnotationTester2 {

    public static void main(String[] args)
            throws Throwable {
        Initializer2.initialize(AnnotationTester2.class);
    }
}
