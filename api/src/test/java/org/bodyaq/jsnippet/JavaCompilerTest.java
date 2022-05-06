package org.bodyaq.jsnippet;

import com.squareup.javapoet.*;
import org.bodyaq.jsnippet.compile.ByteArrayJavaFileObject;
import org.bodyaq.jsnippet.compile.MemoryJavaCompiler;
import org.bodyaq.jsnippet.execute.MemoryClassExecutor;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.Collection;

import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class JavaCompilerTest {

  @Test
  void javaCompilerApiTest() {
    JavaFileObject javaFileObject = getJavaFileObject();

    MemoryJavaCompiler compiler = new MemoryJavaCompiler();
    Collection<ByteArrayJavaFileObject> compiledClasses = compiler.compile(javaFileObject);
    String name = javaFileObject.getName();
    name = name.substring(0, name.indexOf('.'));
    new MemoryClassExecutor().execute(compiledClasses, name);
  }

  JavaFileObject getJavaFileObject() {
    MethodSpec mainMethod =
        MethodSpec.methodBuilder("main")
            .addModifiers(PUBLIC, STATIC)
            .returns(TypeVariableName.VOID)
            .addParameter(ArrayTypeName.get(String[].class), "args")
            .addStatement("System.out.println(\"Hello from memory!\")")
            .build();

    TypeSpec type = TypeSpec.classBuilder("MyClass").addModifiers(PUBLIC).addMethod(mainMethod).build();

    return JavaFile.builder("", type).skipJavaLangImports(true).build().toJavaFileObject();
  }
}
