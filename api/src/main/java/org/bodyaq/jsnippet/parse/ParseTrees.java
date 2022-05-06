package org.bodyaq.jsnippet.parse;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.Tree;

public class ParseTrees {
    public static <T extends Tree> boolean hasParentOfType(Tree tree, Class<T> type) {
        return findParentOfType(tree, type) != null;
    }

    public static <T extends Tree>
    Tree findParentOfType(Tree tree, Class<T> type) {
        Tree parent = tree.getParent();
        while (parent != null) {
            if (parent.getClass().isAssignableFrom(type)) {
                return parent;
            }
            parent = parent.getParent();
        }
        return parent;
    }

    public static String getFullText(ParserRuleContext ctx) {
        if (ctx.start == null || ctx.stop == null ||
            ctx.start.getStartIndex() < 0 || ctx.stop.getStopIndex() < 0) {
            return ctx.getText();
        }

        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }

}
