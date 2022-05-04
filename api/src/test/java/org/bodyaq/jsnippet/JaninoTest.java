package org.bodyaq.jsnippet;

import lombok.SneakyThrows;
import org.codehaus.janino.ClassBodyEvaluator;
import org.codehaus.janino.ScriptEvaluator;
import org.junit.jupiter.api.Test;

public class JaninoTest {

    @Test
    @SneakyThrows
    void scriptEvaluation() {
        ScriptEvaluator se = new ScriptEvaluator();

        se.cook(
            """
            static void method1() {
                System.out.println(1);
            }

            method1();
            method2();

            static void method2() {
                System.out.println(2);
            }
            """
        );

        se.evaluate(new Object[0]);
    }


    @Test
    @SneakyThrows
    void classBodyEvaluation() {
        String classBody =
            """
            class MyClass {
                public static void main(String[] args) {
                    System.out.println("Hello from no longer compiled evaluated class");
                }
            }
            """;

        var evaluator = new ClassBodyEvaluator();
        evaluator.cook(classBody);
    }
}
