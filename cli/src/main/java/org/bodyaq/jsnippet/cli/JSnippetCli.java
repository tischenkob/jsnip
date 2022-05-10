package org.bodyaq.jsnippet.cli;

import org.bodyaq.jsnippet.JSnippet;

import java.io.File;

import static picocli.CommandLine.*;

@Command(name = "jsnippet", mixinStandardHelpOptions = true)
class JSnippetCli implements Runnable {

    @Option(names = {"-j", "--java"})
    boolean exportJava = false;

    @Option(names = {"-c", "--class"})
    boolean exportClass = false;

    @Option(names = {"-n", "--native"})
    boolean exportNative = false;

    @Parameters(index = "0")
    File scriptFile;

    @Parameters(index = "1..")
    String[] args;

    @Override
    public void run() {
        JSnippet snippet = new JSnippet(scriptFile, args);
        snippet.setExportJava(exportJava);
        snippet.setExportClass(exportClass);
        snippet.setExportNative(exportNative);
        snippet.run();
    }
}
