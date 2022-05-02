package org.bodyaq.jsnippet;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.bodyaq.antlr.java.JavaLexer;
import org.bodyaq.antlr.java.JavaParser;

import static org.bodyaq.antlr.java.JavaParser.*;

public class JSnippet {
  public static void main(String[] args) {
    String code = """
                  void printHello() {
                    System.out.println("Hello");
                  }
                  """;

    System.out.println(parseQuery(code));
  }

  public static String parseQuery(String text) {
    JavaParser parser = parserFor(text);

    MethodDeclarationContext methodContext = parser.methodDeclaration();
    var walker = new ParseTreeWalker();
    var listener = new SnippetCollector();

    walker.walk(listener, methodContext);

    return listener.getMethods().toString();
  }

  private static JavaParser parserFor(String raw) {
    CharStream charStream = CharStreams.fromString(raw);
    var lexer = new JavaLexer(charStream);
    var tokens = new CommonTokenStream(lexer);
    return new JavaParser(tokens);
  }

}
