package Analyzer;

import java.util.Arrays;
import java.util.List;

public class Analyzer {

    private static final List<String> JDK_PACKAGE_PREFIXES = Arrays.asList(
        "com.sum.", "java", "javax", "jdk", "org.w3c", "org.xml"
    );

    public static PopupTypeInfo createPopupTypeInfoFromClass(Class<?> inputClass) {
        PopupTypeInfo popupTypeInfo = new PopupTypeInfo();

        popupTypeInfo.setInterface(inputClass.isInterface())
            .setEnum(inputClass.isEnum())
            .setName(inputClass.getSimpleName())
            .setJdk(isJdkClass(inputClass))
            .allAllInheritedClassNames(getAllInheritedClassNames(inputClass));

        return popupTypeInfo;
    }

    public static boolean isJdkClass(Class<?> inputClass) {
        return JDK_PACKAGE_PREFIXES.stream()
            .anyMatch(packagePrefix -> inputClass.getPackage() == null ||
                inputClass.getPackage().getName().startsWith(packagePrefix));
    }

    public static String[] getAllInheritedClassNames(Class<?> inputClass) {
        String[] inheritedClasses;
        if (inputClass.isInterface()) {
            inheritedClasses = Arrays.stream(inputClass.getInterfaces())
                .map(Class::getSimpleName)
                .toArray(String[]::new);
        } else {
            Class<?> inheritedClass = inputClass.getSuperclass();
            inheritedClasses = inheritedClass != null ? new String[] {
                inputClass.getSuperclass().getSimpleName()
            } : null;
        }
        return inheritedClasses;
    }
}
