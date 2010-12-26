/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.regexp;

/**
 * Translates JavaScript RegExp to Java RegExp.<br>
 * // [...\b...] -> [...\cH...]
 * // [...[...] -> [...\[...]
 * // [^\\1] -> .
 * // back reference in character classes are simply ignored by browsers [...ab\5cd...] -> [...abcd...]
 * // characters escaped without need should be "un-escaped"
 * Escape curly braces that are not used in an expression
 * like "{n}", "{n,}" or "{n,m}" (where n and m are positive integers).
 *
 * @version $Revision$
 * @author Ronald Brill
 */
public class RegExpJsToJavaConverter {

    private static final String DIGITS = "0123456789";

    /**
     * Helper to encapsulate the transformations.
     */
    private class Tape {
        private StringBuilder tape_;
        private int currentPos_;

        /**
         * Wraps a JavaScript RegExp to access it char by char like a tape.
         *
         * @param input the Javascript RegExp
         */
        public Tape(final String input) {
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
         * Replaces the current char with the given string.
         * @param token the string to insert
         */
        public void replace(final String token) {
            tape_.replace(currentPos_, currentPos_ + 1, token);
        }

        /**
         * Read the whole tape content.
         *
         * @return tape content
         */
        public String toString() {
            return tape_.toString();
        }
    }

    private Tape tape_;
    private boolean insideCharClass_;
    private boolean insideRepetition_;
    private int noOfSubexpressions_;

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
        noOfSubexpressions_ = 0;

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
                            tape_.replace("");
                            tape_.replace("");
                            tape_.replace(".");
                            insideCharClass_ = false;
                        }
                    }
                }
                else {
                    tape_.move(-1);
                }
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
        else if ('}' == next) {
            tape_.insert("\\", -2);
            tape_.insert("\\", -1);
        }
        else {
            tape_.insert("\\", -2);
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
            noOfSubexpressions_++;
            tape_.move(-1);
            return;
        }

        next = tape_.read();
        if (next < 0) {
            return;
        }
        if (':' != next) {
            noOfSubexpressions_++;
            tape_.move(-1);
            return;
        }
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
            tape_.replace("");
            tape_.move(1);
            return;
        }

        if (insideCharClass_ && 'b' == escapeSequence) {
            // [...\b...] -> [...\cH...]
            tape_.move(-1);
            tape_.replace("cH");
            tape_.move(2);
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
            }
        }

        final int value = Integer.parseInt(tmpNo.toString());
        if (value > noOfSubexpressions_) {
            // we have a octal here
            tape_.insert("0", tmpInsertPos);
            return false;
        }

        if (insideCharClass_) {
            // drop back reference
            for (int i = tmpInsertPos; i <= 0; i++) {
                tape_.move(-1);
                tape_.replace("");
            }
        }
        return true;
    }
}
