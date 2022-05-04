package org.bodyaq.jsnippet.parser.listener;

import org.bodyaq.antlr.java.JavaParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.bodyaq.antlr.java.JavaParser.*;
import static org.bodyaq.jsnippet.parser.ParseTrees.hasParentOfType;

public class StatementCollector extends JavaParserBaseListener {
    private final List<BlockStatementContext> statements = new ArrayList<>();

    @Override
    public void enterBlockStatement(BlockStatementContext ctx) {
        boolean methodChild = hasParentOfType(ctx, MethodDeclarationContext.class);
        boolean blockChild = hasParentOfType(ctx, StatementContext.class);

        if (!methodChild && !blockChild) {
            statements.add(ctx);
        }
    }

    public List<BlockStatementContext> getStatements() {
        return statements;
    }
}
