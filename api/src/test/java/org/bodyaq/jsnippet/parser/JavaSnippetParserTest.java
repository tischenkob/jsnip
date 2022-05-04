package org.bodyaq.jsnippet.parser;

import org.junit.jupiter.api.Test;

class JavaSnippetParserTest {

    @Test
    void parse() {

      String code = """
                    import java.util.List;
                    var x = 1 + 1;
                    if (x == 2) {
                      System.out.prinln("Hooray");
                    }
                    
                    int addInts(int a, int b) {
                      return a + b;
                    }
                    """;

        JavaSnippetParser listener = JavaSnippetParser.parse(code);
    }
}