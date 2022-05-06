module org.bodyaq.jsnip.cli {
    exports org.bodyaq.jsnippet.cli;
    requires org.bodyaq.jsnip;
    requires info.picocli;

    opens org.bodyaq.jsnippet.cli to info.picocli;
}