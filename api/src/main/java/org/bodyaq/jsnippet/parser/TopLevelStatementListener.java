package org.bodyaq.jsnippet.parser;

import org.bodyaq.antlr.java.JavaParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.bodyaq.antlr.java.JavaParser.*;
import static org.bodyaq.jsnippet.parser.ParseTrees.hasParentOfType;

public class TopLevelStatementListener extends JavaParserBaseListener {

  private final List<StatementContext> statements = new ArrayList<>();

  @Override
  public void enterStatement(StatementContext ctx) {
    boolean methodChild = hasParentOfType(ctx, MethodDeclarationContext.class);
    boolean blockChild = hasParentOfType(ctx, BlockContext.class);

    if (!methodChild && !blockChild) {
      statements.add(ctx);
    }
  }

  public List<StatementContext> getStatements() {
    return statements;
  }
}
