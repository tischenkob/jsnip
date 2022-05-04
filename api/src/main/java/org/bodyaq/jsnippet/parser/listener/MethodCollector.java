package org.bodyaq.jsnippet.parser.listener;

import org.bodyaq.antlr.java.JavaParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.bodyaq.antlr.java.JavaParser.*;
import static org.bodyaq.jsnippet.parser.ParseTrees.hasParentOfType;

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
