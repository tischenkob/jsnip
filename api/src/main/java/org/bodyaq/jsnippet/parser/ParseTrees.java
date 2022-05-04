package org.bodyaq.jsnippet.parser;

import org.antlr.v4.runtime.tree.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ParseTrees {
  static <T extends Tree> boolean hasParentOfType(@NotNull Tree tree, @NotNull Class<T> type) {
    return findParentOfType(tree, type) != null;
  }

  static <T extends Tree> @Nullable Tree findParentOfType(
      @NotNull Tree tree, @NotNull Class<T> type) {
    Tree parent = tree.getParent();
    while (parent != null) {
      if (parent.getClass().isAssignableFrom(type)) {
        return parent;
      }
      parent = parent.getParent();
    }
    return parent;
  }
}
