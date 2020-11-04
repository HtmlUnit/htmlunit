/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.apache.commons.lang3.StringUtils.right;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Tests for {@link HtmlAnchor}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlAnchor3Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_getTextContent() throws Exception {
        final String html
            =   "<html>\n"
	      + "<a href= 'https://www.nextbiometrics.com/news_archive_2020/next-biometrics-receives-follow-up-order-from-business-partner-newland-payment-technology/'>\n"
	      + "<!--[text]-->"
              + "<style>"
	      + "h6.additional-class-1268472203{"
	      + "line-height:  24px;"
  	      + "font-size:  22px;"
	      + "letter-spacing:  -1px;}"
	      + "</style>"
              + "<h6 class=" 
	      + "additional-class-1268472203  bd-content-element"
	      + ">"
              + "<!--{content}-->"
              + "NEXT Biometrics receives follow up order from business partner Newland Payment Technology            <!--{/content}-->"
              + "</h6>   <!--[/text]--> 	</a> \n"	
	      + "</html>";

        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getAnchors();

        assertEquals("NEXT Biometrics receives follow up order from business partner Newland Payment Technology", anchor.getTextContent());
    }
}
