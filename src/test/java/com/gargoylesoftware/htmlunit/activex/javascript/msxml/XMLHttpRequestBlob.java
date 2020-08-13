package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

@RunWith(BrowserRunner.class)
public class XMLHttpRequestBlob extends WebDriverTestCase {
    private final static String VALUE_1_FORM_DATA = "--FormData123456\r\n"
            + "Content-Disposition: form-data; name=key1\r\n\r\n"
            + "value1\r\n";

    private final static String VALUE_2_FORM_DATA = "--FormData123456\r\n"
            + "Content-Disposition: form-data; name=key2\r\n\r\n"
            + "value2\r\n";

    @Test
    public void sendBlob() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType("multipart/form-data; boundary=----formdata123456");
    }

    @Test
    public void sendBlob_emptyMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType("");
    }

    @Test
    public void sendBlob_badMimeType() throws Exception {
        sendBlobWithMimeTypeAndAssertContentType("doesnt/exist");
    }

    private void sendBlobWithMimeTypeAndAssertContentType(final String desiredMimeType) throws Exception {
        startWebServer(getMockWebConnection(), Charset.defaultCharset());
        String url = URL_SECOND.toString();

        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html><head><title>foo</title>"
                + "<script>\n"
                + "  function test() {\n"
                + "    try {\n"
                + "      var xhr = new XMLHttpRequest();\n"
                + "      xhr.open('POST', '"
                + url
                + "', false);\n"
                + "      var chunks = [];\n"
                + "      chunks.push(\"--FormData123456\\r\\n\");\n"
                + "      chunks.push('Content-Disposition: form-data; name=key1\\r\\n\\r\\n');\n"
                + "      chunks.push('value1\\r\\n');\n"
                + "      chunks.push(\"--FormData123456\\r\\n\");\n"
                + "      chunks.push('Content-Disposition: form-data; name=key2\\r\\n\\r\\n');\n"
                + "      chunks.push('value2\\r\\n');\n"
                + "      var blob = new Blob(chunks, {type:'"
                + desiredMimeType
                + "'});\n"
                + "      xhr.send(blob);\n"
                + "  } catch (exception) { \n"
                + "    alert(exception);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "<form id='form'>"
                + "<input type='text' value='something'> "
                + "<input type='field' >"
                + "</form>"
                + "</body></html>";

        loadPageWithAlerts2(html);

        Assert.assertEquals("Never received a call to URL_SECOND", URL_SECOND,
                getMockWebConnection().getLastWebRequest().getUrl());

        final String contentType = getMockWebConnection().getLastWebRequest()
                .getAdditionalHeader(HttpHeader.CONTENT_TYPE);

        String requestBody = getMockWebConnection().getLastWebRequest().getRequestBody();
        Assert.assertEquals(VALUE_1_FORM_DATA + VALUE_2_FORM_DATA, requestBody);

        String expectedMimeType = desiredMimeType;
        if (StringUtils.isBlank(expectedMimeType)) {
            expectedMimeType = null;
        } else if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.XHR_SEND_USE_BLOB_MIMETYPE_AS_CONTENTTYPE)) {
            expectedMimeType = null;
        }

        Assert.assertEquals("Unexpected Content-Type header", expectedMimeType, contentType);
    }

}
