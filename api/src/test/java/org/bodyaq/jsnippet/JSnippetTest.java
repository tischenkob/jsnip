package org.bodyaq.jsnippet;

import com.squareup.javapoet.JavaFile;
import org.bodyaq.jsnippet.compile.ByteArrayJavaFileObject;
import org.bodyaq.jsnippet.compile.MemoryJavaCompiler;
import org.bodyaq.jsnippet.execute.MemoryClassExecutor;
import org.bodyaq.jsnippet.template.JavaFiles;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.bodyaq.jsnippet.parse.ScriptParser.ScriptContent;
import static org.bodyaq.jsnippet.parse.ScriptParser.parse;

class JSnippetTest {
    @Test
    void givenSingleSnippet_shouldProduceFullFile() {
        String snippet = """
                         String x = args[0] + " world";
                         println("Something + world = " + x);
                         """;

        ScriptContent collector = parse(snippet);

        JavaFile file = JavaFiles.newFromScript("Snippet", collector);

        MemoryJavaCompiler compiler = new MemoryJavaCompiler();
        Collection<ByteArrayJavaFileObject> compiledClasses = compiler.compile(file.toJavaFileObject());

        new MemoryClassExecutor().execute(compiledClasses, "Snippet", "Hello");
    }

    @Test
    void someBrokentest() {
        String snippet = """
                         println("Enter your name");
                         String name = "mister";
                         int repeatTimes = Integer.parseInt(args[0]);
                         for (int i = 0; i < repeatTimes; i++) {
                             sayHello(name);
                         }
                                                  
                         void sayHello(String name) {
                             println("Hello " + name + "!");
                         }
                         """;

        ScriptContent collector = parse(snippet);

        JavaFile file = JavaFiles.newFromScript("Snippet", collector);

        MemoryJavaCompiler compiler = new MemoryJavaCompiler();
        Collection<ByteArrayJavaFileObject> compiledClasses = compiler.compile(file.toJavaFileObject());

        new MemoryClassExecutor().execute(compiledClasses, "Snippet", "3");
    }
}