package org.bodyaq.jsnippet.cli;

import picocli.CommandLine;

public class JSnippetCliLauncher {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JSnippetCli()).execute(args);
        System.exit(exitCode);
    }
}
