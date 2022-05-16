package org.bodyaq.jsnippet.parse;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.bodyaq.antlr.java.JavaLexer;
import org.bodyaq.antlr.java.JavaParser;
import org.bodyaq.jsnippet.parse.listen.MethodCollector;
import org.bodyaq.jsnippet.parse.listen.ProxyParseTreeListener;
import org.bodyaq.jsnippet.parse.listen.StatementCollector;

import java.util.List;

import static org.bodyaq.antlr.java.JavaParser.*;
import static org.bodyaq.antlr.java.JavaParser.BlockStatementContext;
import static org.bodyaq.antlr.java.JavaParser.MethodDeclarationContext;

public class ScriptParser {

    public static ScriptContent parse(String code) {
        JavaParser parser = parserFor(code);

        CompilationUnitContext context = parser.compilationUnit();

        var statementCollector = new StatementCollector();
        var methodCollector = new MethodCollector();
        var proxyListener = new ProxyParseTreeListener(
            statementCollector,
            methodCollector
        );

        ParseTreeWalker.DEFAULT.walk(proxyListener, context);

        return new ScriptContent(
            statementCollector.getStatements(),
            methodCollector.getMethods()
        );
    }

    private static JavaParser parserFor(String raw) {
        CharStream charStream = CharStreams.fromString(raw);
        var lexer = new JavaLexer(charStream);
        var tokens = new CommonTokenStream(lexer);
        return new JavaParser(tokens);
    }

    public record ScriptContent(
        List<BlockStatementContext> statements,
        List<MethodDeclarationContext> methods
    ) {}
}
