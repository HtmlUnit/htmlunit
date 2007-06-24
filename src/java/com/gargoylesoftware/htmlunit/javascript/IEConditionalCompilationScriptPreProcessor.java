/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A basic implementation for IE Conditional Compilation.
 * 
 * <p>Currently supports only <b>@cc_on</b>, but not <b>@if</b> or <b>@set</b>.
 * 
 * @version $Revision$
 * @author Ahmed Ashour
 * 
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ahx1z4fs(VS.80).aspx">Microsoft Docs</a>
 */
public class IEConditionalCompilationScriptPreProcessor implements ScriptPreProcessor {

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, String sourceCode,
            final String sourceName, final HtmlElement htmlElement) {
        final SortedSet textConstants = initTextConstants( sourceCode );
        int index = -1;
        while( ( index = sourceCode.indexOf( "/*@cc_on", index + 1 ) ) != -1 ) {
            addTextConstant(textConstants, index, index + "/*@cc_on".length(), "");
        }
        index = -1;
        while( ( index = sourceCode.indexOf( "@*/", index + 1 ) ) != -1 ) {
            addTextConstant(textConstants, index, index + "@*/".length(), "" );
        }
        sourceCode = restoreTextConstants(sourceCode, textConstants);
        return sourceCode;
    }

    /**
     * A class to represent a range of text to be replaced.
     */
    private static class TextConstant implements Comparable {

        /**
         * Inclusive
         */
        private final int startIndex_;
        
        /**
         * Exclusive
         */
        private final int endIndex_;
        
        private final String textToReplace_;
        
        TextConstant( final int startIndex, final int endIndex, final String textToReplace ) {
            this.startIndex_ = startIndex;
            this.endIndex_ = endIndex;
            this.textToReplace_ = textToReplace;
        }

        public int compareTo( final Object o) {
            return startIndex_ - ((TextConstant)o).startIndex_;
        }
    }
    

    /**
     * Add the specified values in the given textConstants if it does not intersect
     * with previously added textConstants.
     * 
     * @param textConstants set of textconats to add the following values as a TextConstant to it.
     * @param startIndex starting index
     * @param endIndex end index
     * @param textToReplace text to use
     */
    private void addTextConstant( final Set textConstants, final int startIndex,
            final int endIndex, final String textToReplace ) {
        for( final Iterator it = textConstants.iterator(); it.hasNext(); ) {
            final TextConstant c = (TextConstant)it.next();
            if( startIndex >= c.startIndex_ && startIndex < c.endIndex_ ) {
                return;
            }
        }
        textConstants.add( new TextConstant( startIndex, endIndex, textToReplace) );
    }

    /**
     * Stores all text constants, i.g. everything inside '' and "",
     * so that they are not replaced by subsequent changes.
     */
    private SortedSet initTextConstants( final String sourceCode ) {
        final SortedSet textConstants = new TreeSet();
        char boundaryChar = 0;
        int lastStartIndex = -1;
        for( int index=0; index < sourceCode.length(); index++ ) {
            final char currentChar = sourceCode.charAt( index );
            if( currentChar == '\'' || currentChar == '"' ) {
                if( boundaryChar == 0 && index < sourceCode.length() - 1) {
                    boundaryChar = currentChar;
                    lastStartIndex = index + 1;
                }
                else {
                    boundaryChar = 0;
                    textConstants.add( new TextConstant(
                            lastStartIndex, index, sourceCode.substring( lastStartIndex, index ) ) );
                }
            }
        }
        return textConstants;
    }

    private String restoreTextConstants( String sourceCode, final SortedSet textConstants ) {
        int variation = 0;
        for( final Iterator iterator = textConstants.iterator(); iterator.hasNext(); ) {
            final TextConstant constant = (TextConstant)iterator.next();
            sourceCode = sourceCode.substring( 0, constant.startIndex_+ variation )
                + constant.textToReplace_
                + sourceCode.substring( constant.endIndex_+ variation ); 
                        
            variation += constant.textToReplace_.length() - (constant.endIndex_ - constant.startIndex_);
        }
        return sourceCode;
    }

}
