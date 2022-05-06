package org.bodyaq.jsnippet.execute;

import org.bodyaq.jsnippet.compile.ByteArrayJavaFileObject;

import java.lang.invoke.MethodHandle;
import java.util.Collection;

import static java.lang.invoke.MethodHandles.publicLookup;
import static java.lang.invoke.MethodType.methodType;

public class MemoryClassExecutor {
    public void execute(Collection<ByteArrayJavaFileObject> compiledClasses, String mainClassName, String... args) {
        try {
            Class<?> generatedClass = Class.forName(mainClassName, true, new MemoryClassLoader(compiledClasses));
            MethodHandle mainMethodHandle = publicLookup().findStatic(generatedClass, "main", methodType(void.class, String[].class));
            mainMethodHandle.invoke((Object[]) args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
