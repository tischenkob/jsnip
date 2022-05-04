package org.bodyaq.jsnippet.parser.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.antlr.v4.runtime.ParserRuleContext;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProxyParseTreeListener implements ParseTreeListener {
    private List<ParseTreeListener> listeners;

    public ProxyParseTreeListener(ParseTreeListener... listeners) {
        this(Arrays.asList(listeners));
    }

    public ProxyParseTreeListener(List<ParseTreeListener> listeners) {
        setListeners(listeners);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        for (ParseTreeListener listener : getListeners()) {
            listener.enterEveryRule(ctx);
            ctx.enterRule(listener);
        }
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        for (ParseTreeListener listener : getListeners()) {
            ctx.exitRule(listener);
            listener.exitEveryRule(ctx);
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        for (ParseTreeListener listener : getListeners()) {
            listener.visitErrorNode(node);
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        for (ParseTreeListener listener : getListeners()) {
            listener.visitTerminal(node);
        }
    }

    public void add(ParseTreeListener listener) {
        getListeners().add(listener);
    }

    public boolean remove(ParseTreeListener listener) {
        return getListeners().remove(listener);
    }

    private List<ParseTreeListener> getListeners() {
        return this.listeners;
    }

    public void setListeners(List<ParseTreeListener> listeners) {
        if (listeners == null) {
            listeners = createParseTreeListenerList();
        }

        this.listeners = listeners;
    }

    protected List<ParseTreeListener> createParseTreeListenerList() {
        return new CopyOnWriteArrayList<>();
    }
}