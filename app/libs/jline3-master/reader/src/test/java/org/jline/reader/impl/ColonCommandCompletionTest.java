/*
 * Copyright (c) 2002-2021, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.reader.impl;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ColonCommandCompletionTest extends ReaderTestSupport {

    @Test
    public void testColonCommandCompletion() throws IOException {
        reader.setCompleter(new ColonCommandCompleter("power", "paste"));
        assertBuffer(":paste", new TestBuffer(":p\t\t"));
        assertBuffer(":power ", new TestBuffer(":po\t"));
    }

    private static class ColonCommandCompleter implements Completer {
        String[] commands;

        public ColonCommandCompleter(String... commands) {
            this.commands = commands;
        }

        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            for (String c : commands) {
                candidates.add(new Candidate(":" + AttributedString.stripAnsi(c), c, null, null
                        , null, null, true));
            }
        }
    }
}
