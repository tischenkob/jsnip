package org.bodyaq.jsnippet.cli;

import org.bodyaq.jsnippet.JSnippet;

import java.io.File;

import static picocli.CommandLine.*;

@Command(name = "jsnippet", mixinStandardHelpOptions = true)
class JSnippetCli implements Runnable {

    @Option(names = {"-j", "--export-java"})
    boolean exportJava = false;

    @Parameters(index = "0")
    File scriptFile;

    @Parameters(index = "1..")
    String[] args;

    @Override
    public void run() {
        JSnippet snippet = new JSnippet(scriptFile, args);
        snippet.setExportJava(exportJava);
        snippet.run();
    }
}
