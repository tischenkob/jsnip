package org.bodyaq.jsnippet.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.bodyaq.antlr.java.JavaLexer;
import org.bodyaq.antlr.java.JavaParser;
import org.bodyaq.antlr.java.JavaParserBaseListener;

import static org.bodyaq.antlr.java.JavaParser.*;

public class JavaSnippetParser extends JavaParserBaseListener {

  public static JavaSnippetParser parse(String code) {
    JavaParser parser = parserFor(code);

    var context = parser.compilationUnit();
    var walker = ParseTreeWalker.DEFAULT;
    var listener = new TopLevelStatementListener();

    walker.walk(listener, context);

    listener.getStatements().forEach(stmt -> System.out.println("Found a statement: " + stmt));

    return new JavaSnippetParser();
  }

  private static JavaParser parserFor(String raw) {
    CharStream charStream = CharStreams.fromString(raw);
    var lexer = new JavaLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    return new JavaParser(tokens);
  }

  @Override
  public void enterImportDeclaration(ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
  }

  @Override
  public void enterClassBody(ClassBodyContext ctx) {
    super.enterClassBody(ctx);
  }

  @Override
  public void enterMethodDeclaration(MethodDeclarationContext ctx) {
    ctx.methodBody().getText();
  }

  @Override
  public void enterMethodBody(MethodBodyContext ctx) {
    super.enterMethodBody(ctx);
  }

  @Override
  public void enterStatement(StatementContext ctx) {
    super.enterStatement(ctx);
  }
}
