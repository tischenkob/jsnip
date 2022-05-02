package org.bodyaq.jsnippet;

import org.bodyaq.antlr.java.*;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.*;

public class SnippetCollector extends JavaParserBaseListener {
    private final Set<String> methods = new HashSet<>();
    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        methods.add(ctx.methodBody().getText());
    }

    public Set<String> getMethods() {
        return unmodifiableSet(methods);
    }

}
