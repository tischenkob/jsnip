package org.bodyaq.jsnippet;

import com.squareup.javapoet.JavaFile;
import org.bodyaq.jsnippet.parser.JavaSnippetParser;
import org.bodyaq.jsnippet.template.JavaFiles;
import org.junit.jupiter.api.Test;

class JSnippetTest {
    @Test
    void givenSingleSnippet_shouldProduceFullFile() {
        String snippet = """
                         var x = 1 + 1;
                         println("1 + 1 = " + x);
                         """;

        JavaSnippetParser collector = JavaSnippetParser.parse(snippet);

        JavaFile file = JavaFiles.generateFromTemplate("Snippet.java");

    }
}