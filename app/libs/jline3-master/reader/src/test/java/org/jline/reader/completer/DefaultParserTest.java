/*
 * Copyright (c) 2002-2016, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.reader.completer;

import java.util.Arrays;
import java.util.Collections;

import org.jline.reader.EOFError;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.ReaderTestSupport;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultParser}.
 *
 * @author <a href="mailto:mdrob@apache.org">Mike Drob</a>
 */
public class DefaultParserTest extends ReaderTestSupport {

    ParsedLine delimited;
    DefaultParser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testDelimit() {
        // These all passed before adding quoting and escaping
        delimited = parser.parse("1 2 3", 0);
        assertEquals(Arrays.asList("1", "2", "3"), delimited.words());

        delimited = parser.parse("1  2  3", 0);
        assertEquals(Arrays.asList("1", "2", "3"), delimited.words());
    }

    @Test
    public void testQuotedDelimit() {
        delimited = parser.parse("\"1 2\" 3", 0);
        assertEquals(Arrays.asList("1 2", "3"), delimited.words());

        delimited = parser.parse("'1 2' 3", 0);
        assertEquals(Arrays.asList("1 2", "3"), delimited.words());

        delimited = parser.parse("1 '2 3'", 0);
        assertEquals(Arrays.asList("1", "2 3"), delimited.words());

        delimited = parser.parse("0'1 2' 3", 0);
        assertEquals(Arrays.asList("0'1 2'", "3"), delimited.words());

        delimited = parser.parse("'01 '2 3", 0);
        assertEquals(Arrays.asList("01 2", "3"), delimited.words());
    }

    @Test
    public void testMixedQuotes() {
        delimited = parser.parse("\"1' '2\" 3", 0);
        assertEquals(Arrays.asList("1' '2", "3"), delimited.words());

        delimited = parser.parse("'1\" 2' 3\"", 0);
        assertEquals(Arrays.asList("1\" 2", "3\""), delimited.words());
    }

    @Test
    public void testEscapedSpace() {
        delimited = parser.parse("1\\ 2 3", 0);
        assertEquals(Arrays.asList("1 2", "3"), delimited.words());
    }

    @Test
    public void testEscapedQuotes() {
        delimited = parser.parse("'1 \\'2' 3", 0);
        assertEquals(Arrays.asList("1 '2", "3"), delimited.words());

        delimited = parser.parse("\\'1 '2' 3", 0);
        assertEquals(Arrays.asList("'1", "2", "3"), delimited.words());

        delimited = parser.parse("'1 '2\\' 3", 0);
        assertEquals(Arrays.asList("1 2'", "3"), delimited.words());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStartBlockCommentDelim() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims(null, "*/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEndBlockCommentsDelim() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualBlockCommentsDelims() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", "/*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStartBlockCommentsDelims() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("", "*/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyEndBlockCommentsDelims() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", ""));
    }

    @Test
    public void testBashComments() {
        parser.setLineCommentDelims(new String[] {"#"});
        delimited = parser.parse("1 2 # 3", 0);
        assertEquals(Arrays.asList("1", "2"), delimited.words());

        delimited = parser.parse("#\\'1 '2' 3", 0);
        assertEquals(Collections.emptyList(), delimited.words());

        delimited = parser.parse("'#'\\'1 '2' 3", 0);
        assertEquals(Arrays.asList("#'1", "2", "3"), delimited.words());

        delimited = parser.parse("#1 " + System.lineSeparator() + " '2' 3", 0);
        assertEquals(Arrays.asList("2", "3"), delimited.words());
    }

    @Test
    public void testJavaComments() {
        parser.setLineCommentDelims(new String[] {"//"});
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", "*/"));

        delimited = parser.parse("1 2 # 3", 0);
        assertEquals(Arrays.asList("1", "2", "#", "3"), delimited.words());

        delimited = parser.parse("1 2 // 3", 0);
        assertEquals(Arrays.asList("1", "2"), delimited.words());

        delimited = parser.parse("/*\\'1 \n '2' \n3*/", 0);
        assertEquals(Collections.emptyList(), delimited.words());

        delimited = parser.parse("'//'\\'1 /*'2'\n */3", 0);
        assertEquals(Arrays.asList("//'1", "3"), delimited.words());


        delimited = parser.parse("hello/*comment*/world", 0);
        assertEquals(Arrays.asList("hello", "world"), delimited.words());
    }

    @Test
    public void testSqlComments() {
        // The test check sql line comment --
        // and sql block comments /* */
        parser.setLineCommentDelims(new String[]{"--"});
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", "*/"));

        delimited = parser.parse("/*/g */", 0);
        assertEquals(Collections.emptyList(), delimited.words());

        delimited = parser.parse("/**/g", 0);
        assertEquals(Arrays.asList("g"), delimited.words());

        delimited = parser.parse("select '--';", 0);
        assertEquals(Arrays.asList("select", "--;"), delimited.words());
        delimited = parser.parse("select --; '--';", 0);
        assertEquals(Arrays.asList("select"), delimited.words());

        delimited = parser.parse("select 1/* 789*/ ; '--';", 0);
        assertEquals(Arrays.asList("select", "1", ";", "--;"), delimited.words());
        delimited = parser.parse("select 1/* 789 \n */ ; '--';", 0);
        assertEquals(Arrays.asList("select", "1", ";", "--;"), delimited.words());

        delimited = parser.parse("select 1/* 789 \n * / ; '--';*/", 0);
        assertEquals(Arrays.asList("select", "1"), delimited.words());
        delimited = parser.parse("select '1';--comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';-----comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';--comment\n", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';--comment\n\n", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1'; --comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';\n--comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';\n\n--comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';\n \n--comment", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1'\n;\n--comment", 0);
        assertEquals(Arrays.asList("select", "1", ";"), delimited.words());

        delimited = parser.parse("select '1'\n\n;--comment", 0);
        assertEquals(Arrays.asList("select", "1", ";"), delimited.words());

        delimited = parser.parse("select '1'\n\n;---comment", 0);
        assertEquals(Arrays.asList("select", "1", ";"), delimited.words());

        delimited = parser.parse("select '1'\n\n;-- --comment", 0);
        assertEquals(Arrays.asList("select", "1", ";"), delimited.words());

        delimited = parser.parse("select '1'\n\n;\n--comment", 0);
        assertEquals(Arrays.asList("select", "1", ";"), delimited.words());

        delimited = parser.parse("select '1'/*comment*/", 0);
        assertEquals(Arrays.asList("select", "1"), delimited.words());

        delimited = parser.parse("select '1';/*---comment */", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';/*comment\n*/\n", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1';/*comment*/\n\n", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1'; /*--comment*/", 0);
        assertEquals(Arrays.asList("select", "1;"), delimited.words());

        delimited = parser.parse("select '1/*' as \"asd\";", 0);
        assertEquals(Arrays.asList("select", "1/*", "as", "asd;"), delimited.words());

        delimited = parser.parse("select '/*' as \"asd*/\";", 0);
        assertEquals(Arrays.asList("select", "/*", "as", "asd*/;"), delimited.words());

        delimited = parser.parse("select '1' as \"'a'\\\ns'd\\\n\n\" from t;", 0);
        assertEquals(Arrays.asList("select", "1", "as", "'a'\ns'd\n\n", "from", "t;"), delimited.words());
    }

    @Test(expected = EOFError.class)
    public void testMissingOpeningBlockComment() {
        parser.setBlockCommentDelims(new DefaultParser.BlockCommentDelims("/*", "*/"));
        delimited = parser.parse("1, 2, 3 */", 0);
    }
}
