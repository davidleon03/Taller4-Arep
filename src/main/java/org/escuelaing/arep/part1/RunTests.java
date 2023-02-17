package org.escuelaing.arep.part1;


import java.lang.reflect.Method;

public class RunTests {
    public static void main(String[] args) throws Exception {
        int passed = 0, failed = 0;
        for (String arg: args) {
            for (Method m : Class.forName(arg).getMethods()) {
                if (m.isAnnotationPresent(Test.class)) {
                    try {
                        m.invoke(null);
                        passed++;
                    } catch (Throwable ex) {
                        System.out.printf("Test %s failed: %s %n", m, ex.getCause());
                        failed++;
                    }
                }
            }
            System.out.printf("Passed: %d, Failed %d%n", passed, failed);
        }
    }
}