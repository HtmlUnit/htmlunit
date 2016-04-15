package com.gargoylesoftware.htmlunit.javascript.host.event;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link AnimationEvent}
 *
 * @author Ahmed Ashour
 * @author Madis Pärn
 */
@RunWith(BrowserRunner.class)
public class AnimationEventTest extends WebDriverTestCase{

    /**
     * Test for feature-request #229.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object AnimationEvent]")
    @NotYetImplemented
    public void end() throws Exception {
        final String html
            = "<html><head>\n"
            + "<style>\n"
            + "  .animate {  animation: identifier .1s ; }\n"
            + "  @keyframes identifier {\n"
            + "    0% { width: 0px; }\n"
            + "    100% { width: 30px; }\n"
            + "  }\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var el = document.getElementById('div1');\n"
            + "  el.addEventListener('animationend', function(e) {\n"
            + "    alert(e);\n"
            + "  });\n"
            + "  el.className = 'animate';\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='div1'>TXT</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
