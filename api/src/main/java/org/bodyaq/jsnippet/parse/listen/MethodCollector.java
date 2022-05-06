package org.bodyaq.jsnippet.parse.listen;

import org.bodyaq.antlr.java.JavaParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.bodyaq.antlr.java.JavaParser.ClassDeclarationContext;
import static org.bodyaq.antlr.java.JavaParser.MethodDeclarationContext;
import static org.bodyaq.jsnippet.parse.ParseTrees.hasParentOfType;

public class MethodCollector extends JavaParserBaseListener {
    public List<MethodDeclarationContext> getMethods() {
        return methods;
    }

    private final List<MethodDeclarationContext> methods = new ArrayList<>();

    @Override
    public void enterMethodDeclaration(MethodDeclarationContext ctx) {
        if (!hasParentOfType(ctx, ClassDeclarationContext.class)) {
            methods.add(ctx);
        }
    }

}
