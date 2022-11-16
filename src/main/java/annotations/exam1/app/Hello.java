package annotations.exam1.app;

import annotations.exam1.Initializer.InitializeMethod;

public class Hello {

    @InitializeMethod
    public void hello() {
        System.out.println("hello world");
    }
}
