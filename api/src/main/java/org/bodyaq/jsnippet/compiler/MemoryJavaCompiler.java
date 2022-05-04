package org.bodyaq.jsnippet.compiler;

import javax.tools.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.tools.JavaCompiler.CompilationTask;
import static javax.tools.JavaFileObject.Kind;

public class MemoryJavaCompiler {

  public Collection<ByteArrayJavaFileObject> compile(JavaFileObject inputClass) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    var compiledClasses = new ArrayList<ByteArrayJavaFileObject>();

    StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

    var fileManager =
        new ForwardingJavaFileManager<>(stdFileManager) {
          @Override
          public JavaFileObject getJavaFileForOutput(
              Location l, String className, Kind kind, FileObject o) throws IOException {
            if (kind == Kind.CLASS) {
              var compiledClass = new ByteArrayJavaFileObject(className);
              compiledClasses.add(compiledClass);
              return compiledClass;
            }
            return super.getJavaFileForOutput(l, className, kind, o);
          }
        };

    CompilationTask task =
        compiler.getTask(null, fileManager, null, null, null, List.of(inputClass));

    task.call();

    return compiledClasses;
  }

  public void execute(
      Collection<ByteArrayJavaFileObject> compiledClasses, String mainClassName, String[] args)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    Class.forName(mainClassName, true, new MemoryClassLoader(compiledClasses))
        .getDeclaredMethod("main", String[].class)
        .invoke(null, (Object[]) args);
  }
}
