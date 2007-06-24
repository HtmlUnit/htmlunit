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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for the Screen object.
 * 
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_screen.asp">
 * MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
public class ScreenTest extends WebTestCase {

    /**
     * @param name The name of the test case
     */
    public ScreenTest(final String name) {
        super(name);
    }

    /**
     * Test all desired properties on screen (easy to copy and test in a real browser).
     * @throws Exception on test failure.
     */
    public void testProperties() throws Exception {
        final String content = "<html><head><title>test</title>\n" 
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var props = {\n"
            + "           availHeight: 600, \n"
            + "           availLeft: 0, \n"
            + "           availTop: 0, \n"
            + "           availWidth: 800, \n"
            + "           bufferDepth: 24, \n"
            + "           deviceXDPI: 96, \n"
            + "           deviceYDPI: 96, \n"
            + "           fontSmoothingEnabled: true, \n"
            + "           height: 600, \n"
            + "           left: 0, \n"
            + "           logicalXDPI: 96, \n"
            + "           logicalYDPI: 96, \n" 
            + "           pixelDepth: 24, \n"
            + "           top: 0, \n"
            + "           updateInterval: 0, \n"
            + "           width: 800 \n"
            + "       }; \n"
            + "       var nbTests = 0;\n"
            + "       for (var i in props) {"
            + "           var myExpr = 'window.screen.' + i;\n" 
            + "           var result = eval(myExpr);\n" 
            + "           if (props[i] != result) {" 
            + "               alert(myExpr + ': ' + result + ' != ' + props[i]);\n"
            + "           }\n"
            + "           ++nbTests;\n"
            + "       }\n"
            + "       alert(nbTests);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";
        final List collectedAlerts = new ArrayList();
        loadPage(content, collectedAlerts);
        final String[] expectedAlerts = new String[] {"16"};
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
