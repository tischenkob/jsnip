package org.bodyaq.jsnippet.template;

import com.squareup.javapoet.*;
import org.bodyaq.antlr.java.JavaParser;
import org.bodyaq.jsnippet.parse.ParseTrees;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.squareup.javapoet.MethodSpec.Builder;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.element.Modifier.*;
import static org.bodyaq.antlr.java.JavaParser.MethodDeclarationContext;
import static org.bodyaq.jsnippet.parse.ScriptParser.ScriptContent;

public class JavaFiles {
    private static final ClassName STRING_TYPE = ClassName.get(String.class);

    public static JavaFile newFromScript(String className, ScriptContent scriptContent) {
        Builder runMethod = initRunMethod();
        scriptContent.statements().stream()
            .map(ParseTrees::getFullText)
            .map(s -> s + "\n")
            .forEach(runMethod::addCode);

        TypeSpec.Builder typeBuilder = initTypeBuilder(className);

        typeBuilder.addMethod(runMethod.build());
        scriptContent.methods()
            .stream()
            .map(JavaFiles::buildMethodFromContext)
            .forEach(typeBuilder::addMethod);

        TypeSpec type = typeBuilder.build();

        return JavaFile.builder("", type)
                   .skipJavaLangImports(true)
                   .addStaticImport(Collectors.class, "*")
                   .addStaticImport(Comparator.class, "*")
                   .addStaticImport(Predicate.class, "*")
                   .build();
    }

    private static Builder initRunMethod() {
        return methodBuilder("run")
                   .returns(TypeName.VOID)
                   .addParameter(ArrayTypeName.of(String.class), "args");
    }

    private static TypeSpec.Builder initTypeBuilder(String className) {
        MethodSpec printMethod =
            methodBuilder("println")
                .addModifiers(STATIC)
                .addParameter(STRING_TYPE, "message")
                .addStatement("System.out.println(message)")
                .build();

        FieldSpec scannerField =
            FieldSpec.builder(ClassName.get(Scanner.class), "input")
                .addModifiers(PRIVATE, STATIC, FINAL)
                .initializer("new Scanner(System.in)")
                .build();

        MethodSpec mainMethod = methodBuilder("main")
                                    .addModifiers(PUBLIC, STATIC)
                                    .returns(TypeName.VOID)
                                    .addParameter(ArrayTypeName.of(String.class), "args")
                                    .addStatement("new %s().run(args)".formatted(className))
                                    .build();

        return TypeSpec.classBuilder(className)
                   .addModifiers(PUBLIC)
                   .addMethod(printMethod)
                   .addMethod(mainMethod)
                   .addField(scannerField);
    }

    private static MethodSpec buildMethodFromContext(MethodDeclarationContext method) {
        CodeBlock methodCode = method.methodBody().block()
                                   .blockStatement().stream()
                                   .map(ParseTrees::getFullText)
                                   .collect(collectingAndThen(joining("\n"),
                                                              CodeBlock::of));


        List<ParameterSpec> parameters = method.formalParameters()
                                             .formalParameterList()
                                             .formalParameter().stream()
                                             .map(JavaFiles::toParameterSpec)
                                             .toList();


        String methodName = method.identifier().getText();
        TypeName returnType = typeNameOf(method.typeTypeOrVoid().getText());
        return methodBuilder(methodName)
                   .returns(returnType)
                   .addParameters(parameters)
                   .addCode(methodCode)
                   .build();
    }

    private static ParameterSpec toParameterSpec(JavaParser.FormalParameterContext parameterContext) {
        JavaParser.TypeTypeContext type = parameterContext.typeType();
        JavaParser.VariableDeclaratorIdContext variable = parameterContext.variableDeclaratorId();
        return ParameterSpec.builder(typeNameOf(type.getText()), variable.identifier().getText()).build();
    }

    private static TypeName typeNameOf(String returnType) {
        TypeName typeName;
        try {
            Constructor<TypeName> constructor = TypeName.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            typeName = constructor.newInstance(returnType);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return typeName;
    }
}
