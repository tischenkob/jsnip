module org.bodyaq.jsnip {
    requires org.antlr.antlr4.runtime;
  requires com.squareup.javapoet;
  requires java.compiler;
  requires org.jetbrains.annotations;

    exports org.bodyaq.jsnippet;
  exports org.bodyaq.jsnippet.template;
}
