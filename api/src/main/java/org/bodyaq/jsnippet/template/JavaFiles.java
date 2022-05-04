package org.bodyaq.jsnippet.template;

import com.squareup.javapoet.*;

import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javax.lang.model.element.Modifier.*;

public class JavaFiles {
  private static final ClassName STRING_TYPE = ClassName.get(String.class);

  public static JavaFile generateFromTemplate(String className) {
    return generateFromTemplate(className, null);
  }

  public static JavaFile generateFromTemplate(
      String className, Consumer<TypeSpec.Builder> typeVisitor) {

    TypeSpec.Builder typeBuilder = initTypeBuilder(className);

    if (typeVisitor != null) {
      typeVisitor.accept(typeBuilder);
    }

    TypeSpec type = typeBuilder.build();

    return JavaFile.builder("", type)
        .skipJavaLangImports(true)
        .addStaticImport(Collectors.class, "*")
        .addStaticImport(Comparator.class, "*")
        .addStaticImport(Predicate.class, "*")
        .build();
  }

  private static TypeSpec.Builder initTypeBuilder(String className) {
    MethodSpec printMethod =
        MethodSpec.methodBuilder("println")
            .addModifiers(STATIC)
            .addParameter(STRING_TYPE, "message")
            .addStatement("System.out.println(message)")
            .build();

    FieldSpec scannerField =
        FieldSpec.builder(ClassName.get(Scanner.class), "input")
            .addModifiers(PRIVATE, STATIC, FINAL)
            .initializer("new Scanner(System.in)")
            .build();

    return TypeSpec.classBuilder(className).addMethod(printMethod).addField(scannerField);
  }
}
