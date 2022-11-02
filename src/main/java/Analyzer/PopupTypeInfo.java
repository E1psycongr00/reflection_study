package Analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PopupTypeInfo {

    private final List<String> inheritedClassNames = new ArrayList<>();
    private boolean isPrimitive;
    private boolean isInterface;
    private boolean isEnum;
    private String name;
    private boolean isJdk;

    public PopupTypeInfo allAllInheritedClassNames(String[] inheritedClassNames) {
        if (inheritedClassNames != null) {
            this.inheritedClassNames.addAll(
                Arrays.stream(inheritedClassNames).collect(Collectors.toList()));
        }
        return this;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public PopupTypeInfo setPrimitive(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
        return this;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public PopupTypeInfo setInterface(boolean anInterface) {
        this.isInterface = anInterface;
        return this;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public PopupTypeInfo setEnum(boolean anEnum) {
        isEnum = anEnum;
        return this;
    }

    public String getName() {
        return name;
    }

    public PopupTypeInfo setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isJdk() {
        return isJdk;
    }

    public PopupTypeInfo setJdk(boolean jdk) {
        isJdk = jdk;
        return this;
    }

    public List<String> getInheritedClassNames() {
        return inheritedClassNames;
    }
}
