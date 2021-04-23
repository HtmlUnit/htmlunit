/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.regexp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Translates JavaScript RegExp to Java RegExp.<br>
 * // [...\b...] to [...\cH...]
 * // [...[...] to [...\[...]
 * // [^\\1] to .
 * // back reference in character classes are simply ignored by browsers [...ab\5cd...] to [...abcd...]
 * // characters escaped without need should be "un-escaped"
 * Escape curly braces that are not used in an expression
 * like "{n}", "{n,}" or "{n,m}" (where n and m are positive integers).
 *
 * @author Ronald Brill
 * @author Leszek Hoppe
 * @author Atsushi Nakagawa
 */
public class RegExpJsToJavaConverter {

    private static final String DIGITS = "0123456789";

    private Tape tape_;
    private boolean insideCharClass_;
    private boolean insideRepetition_;
    private Deque<Subexpresion> parsingSubexpressions_;
    private List<Subexpresion> subexpressions_;

    /**
     * Helper to encapsulate the transformations.
     */
    private static class Tape {
        private final StringBuilder tape_;
        private int currentPos_;

        /**
         * Wraps a JavaScript RegExp to access it char by char like a tape.
         *
         * @param input the Javascript RegExp
         */
        Tape(final String input) {
            currentPos_ = 0;
            tape_ = new StringBuilder(input);
        }

        /**
         * Moves the current read position by offset.
         * @param offset the move position offset
         */
        public void move(final int offset) {
            currentPos_ += offset;
        }

        /**
         * Reads the character at the current position and
         * moves the read position by 1.
         *
         * @return the character at current position
         */
        public int read() {
            if (currentPos_ < 0) {
                return -1;
            }
            if (currentPos_ >= tape_.length()) {
                return -1;
            }

            return tape_.charAt(currentPos_++);
        }

        /**
         * Inserts a string at the current position + offset.
         * @param token the string to insert
         * @param offset the move position offset
         */
        public void insert(final String token, final int offset) {
            tape_.insert(currentPos_ + offset, token);
            currentPos_ += token.length();
        }

        /**
         * Inserts a string at the given pos.
         * @param token the string to insert
         * @param pos the move position offset
         */
        public void insertAt(final String token, final int pos) {
            tape_.insert(pos, token);
            currentPos_ += token.length();
        }

        /**
         * Replaces the current char with the given string.
         * @param token the string to insert
         */
        public void replace(final int count, final String token) {
            tape_.replace(currentPos_, currentPos_ + count, token);
            currentPos_ += token.length();
        }

        /**
         * Removes number of chars from the given string.
         * @param count the number of chars to remove
         */
        public void remove(final int count) {
            tape_.delete(currentPos_, currentPos_ + count);
        }

        /**
         * Read the whole tape content.
         *
         * @return tape content
         */
        @Override
        public String toString() {
            return tape_.toString();
        }
    }

    private static final class Subexpresion {
        private boolean closed_;
        private boolean optional_;
        private boolean enhanced_;
        private int start_;
        private int end_;

        Subexpresion() {
            closed_ = false;
            optional_ = false;
            enhanced_ = false;
            start_ = -1;
            end_ = -1;
        }
    }

    /**
     * Initiate the FSM.
     */
    public RegExpJsToJavaConverter() {
        super();
    }

    /**
     * Run the state machine on a given input string.
     *
     * @param input
     *            the js regexp to process
     * @return a valid java regex pattern
     */
    public String convert(final String input) {
        tape_ = new Tape(input);
        insideCharClass_ = false;
        insideRepetition_ = false;

        parsingSubexpressions_ = new ArrayDeque<>();
        subexpressions_ = new LinkedList<>();

        int current = tape_.read();
        while (current > -1) {
            if ('\\' == current) {
                processEscapeSequence();
            }
            else if ('[' == current) {
                processCharClassStart();
            }
            else if (']' == current) {
                processCharClassEnd();
            }
            else if ('{' == current) {
                processRepetitionStart();
            }
            else if ('}' == current) {
                processRepetitionEnd();
            }
            else if ('(' == current) {
                processSubExpressionStart();
            }
            else if (')' == current) {
                processSubExpressionEnd();
            }

            // process next
            current = tape_.read();
        }

        return tape_.toString();
    }

    private void processCharClassStart() {
        if (insideCharClass_) {
            tape_.insert("\\", -1);
        }
        else {
            insideCharClass_ = true;

            int next = tape_.read();
            if (next < 0) {
                tape_.insert("\\", -1);
                return;
            }
            if ('^' == next) {
                // [^\2]
                next = tape_.read();
                if (next < 0) {
                    tape_.insert("\\", -2);
                    return;
                }
                if ('\\' == next) {
                    next = tape_.read();
                    if (DIGITS.indexOf(next) < 0) {
                        tape_.move(-2);
                        return;
                    }
                    // if it was a back reference then we have to check more
                    if (handleBackReferenceOrOctal(next)) {
                        next = tape_.read();
                        if (']' == next) {
                            tape_.move(-3);
                            tape_.replace(3, ".");
                            insideCharClass_ = false;
                        }
                    }
                }
                else if (']' == next) {
                    // [^]
                    tape_.move(-3);
                    tape_.replace(3, ".");
                }
                else {
                    tape_.move(-1);
                }
            }
            else if (']' == next) {
                // []
                tape_.move(-2);
                tape_.replace(2, "(?!)");
            }
            else {
                tape_.move(-1);
            }
        }
    }

    private void processCharClassEnd() {
        insideCharClass_ = false;
    }

    private void processRepetitionStart() {
        final int next = tape_.read();
        if (next < 0) {
            tape_.insert("\\", -1);
            return;
        }

        if (DIGITS.indexOf(next) > -1) {
            insideRepetition_ = true;
        }
        else {
            tape_.insert("\\", -2);
            tape_.move(-1);
        }
    }

    private void processRepetitionEnd() {
        if (insideRepetition_) {
            insideRepetition_ = false;
            return;
        }

        tape_.insert("\\", -1);
    }

    private void processSubExpressionStart() {
        int next = tape_.read();
        if (next < 0) {
            return;
        }

        if ('?' != next) {
            final Subexpresion sub = new Subexpresion();
            sub.start_ = tape_.currentPos_;
            parsingSubexpressions_.push(sub);
            subexpressions_.add(sub);

            tape_.move(-1);
            return;
        }

        next = tape_.read();
        if (next < 0) {
            return;
        }
        if (':' != next) {
            final Subexpresion sub = new Subexpresion();
            sub.start_ = tape_.currentPos_;
            parsingSubexpressions_.push(sub);
            subexpressions_.add(sub);

            tape_.move(-1);
            return;
        }

        final Subexpresion sub = new Subexpresion();
        sub.start_ = tape_.currentPos_;
        parsingSubexpressions_.push(sub);
    }

    private void processSubExpressionEnd() {
        if (parsingSubexpressions_.isEmpty()) {
            return;
        }
        final Subexpresion sub = parsingSubexpressions_.pop();
        sub.closed_ = true;
        sub.end_ = tape_.currentPos_;

        final int next = tape_.read();
        sub.optional_ = '?' == next;
        tape_.move(-1);
    }

    private void processEscapeSequence() {
        final int escapeSequence = tape_.read();
        if (escapeSequence < 0) {
            return;
        }

        if ('x' == escapeSequence) {
            // Hex code (e.g. \x00)
            // Read the two char hex code
            tape_.move(2);
            return;
        }

        if ('u' == escapeSequence) {
            // Unicode (e.g. \u0009)
            // Read the for char unicode
            tape_.move(4);
            return;
        }

        if ("ACEFGHIJKLMNOPQRTUVXYZaeghijklmpqyz".indexOf(escapeSequence) > -1) {
            // no need to escape this chars
            tape_.move(-2);
            tape_.remove(1);
            tape_.move(1);
            return;
        }

        if (insideCharClass_ && 'b' == escapeSequence) {
            // [...\b...] -> [...\cH...]
            tape_.move(-1);
            tape_.replace(1, "cH");
            return;
        }

        if (DIGITS.indexOf(escapeSequence) > -1) {
            handleBackReferenceOrOctal(escapeSequence);
        }
    }

    private boolean handleBackReferenceOrOctal(final int aFirstChar) {
        // first try to figure out what the number is
        final StringBuilder tmpNo = new StringBuilder(Character.toString((char) aFirstChar));
        int tmpInsertPos = -1;
        int next = tape_.read();
        if (next > -1) {
            if (DIGITS.indexOf(next) > -1) {
                tmpNo.append(next);
                tmpInsertPos--;
                next = tape_.read();
                if (next > -1) {
                    if (DIGITS.indexOf(next) > -1) {
                        tmpNo.append(next);
                        tmpInsertPos--;
                    }
                    else {
                        tape_.move(-1);
                    }
                }
            }
            else {
                tape_.move(-1);
                if ('0' == aFirstChar) {
                    // \0 has to be replaced by \x00
                    tape_.insert("x0", -1);
                    return false;
                }
            }
        }
        else {
            // if \0 is last character of pattern
            if ('0' == aFirstChar) {
                // \0 has to be replaced by \x00
                tape_.insert("x0", -1);
                return false;
            }
        }

        if (tmpNo.charAt(0) == '0') {
            // we have a octal here
            return false;
        }

        final int value = Integer.parseInt(tmpNo.toString());
        if (value > subexpressions_.size()) {
            // we have a octal here
            tape_.insert("0", tmpInsertPos);
            return false;
        }

        // ignore invalid back references (inside char classes
        // of if the referenced group is not (yet) available
        if (insideCharClass_
                || (0 < value && value <= subexpressions_.size() && !subexpressions_.get(value - 1).closed_)
                || value > subexpressions_.size()) {
            // drop back reference
            for (int i = tmpInsertPos; i <= 0; i++) {
                tape_.move(-1);
                tape_.remove(1);
            }
        }

        // ok it is a backreference
        final Subexpresion back = subexpressions_.get(value - 1);
        if (back.optional_ &&  !back.enhanced_) {
            // change subexpression to make it java compatible
            int insertPos = back.start_ - 1;
            tape_.insertAt("(?:", insertPos);
            for (final Subexpresion subexp : subexpressions_) {
                if (subexp.start_ > insertPos) {
                    subexp.start_ = subexp.start_ + 3;
                }
                if (subexp.end_ > insertPos) {
                    subexp.end_ = subexp.end_ + 3;
                }
            }

            insertPos = back.end_ + 1;
            tape_.insertAt(")", insertPos);
            for (final Subexpresion subexp : subexpressions_) {
                if (subexp.start_ > insertPos) {
                    subexp.start_ = subexp.start_ + 1;
                }
                if (subexp.end_ > insertPos) {
                    subexp.end_ = subexp.end_ + 1;
                }
            }
        }
        return true;
    }
}
