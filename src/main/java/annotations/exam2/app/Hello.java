package annotations.exam2.app;

import annotations.exam2.Initializer2.InitializeMethod;

public class Hello {

    @InitializeMethod
    public void hello() {
        System.out.println("hello world");
    }
}
