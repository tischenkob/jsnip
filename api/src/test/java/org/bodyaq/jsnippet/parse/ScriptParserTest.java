package org.bodyaq.jsnippet.parse;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bodyaq.jsnippet.parse.ScriptParser.ScriptContent;

class ScriptParserTest {

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

        ScriptContent content = ScriptParser.parse(code);

        assertThat(content.methods()).hasSize(1);
        assertThat(content.statements()).hasSize(2);
    }
}