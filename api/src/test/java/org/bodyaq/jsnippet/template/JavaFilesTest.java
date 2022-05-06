package org.bodyaq.jsnippet.template;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.bodyaq.jsnippet.parse.ScriptParser.ScriptContent;

class JavaFilesTest {

    @Test
    void generateFromTemplate() {
        var file = JavaFiles.newFromScript(
            "MyCoolClass",
            new ScriptContent(List.of(), List.of())
        );

        System.out.println(file);
    }
}