/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Wrapper around a "real" WebConnection that will use the wrapped web connection
 * to do the real job and save all received responses
 * in the temp directory with an overview page.<br>
 * <br>
 * This may be useful at conception time to understand what is "browsed".<br>
 * <br>
 * Example:
 * <pre>
 * final WebClient client = new WebClient();
 * final WebConnection connection = new DebuggingWebConnection(client.getWebConnection(), "myTest");
 * client.setWebConnection(connection);
 * </pre>
 * In this example an overview page will be generated under the name myTest.html in the temp directory.<br>
 * <br>
 * <em>This class is only intended as an help during the conception.</em>
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class DebuggingWebConnection extends WebConnectionWrapper {
    private static final Log LOG = LogFactory.getLog(DebuggingWebConnection.class);

    private int counter_;
    private final WebConnection wrappedWebConnection_;
    private final String reportBaseName_;
    private final File javaScriptFile_;

    /**
     * Wraps a web connection to have a report generated of the received responses.
     * @param webConnection the webConnection that do the real work
     * @param reportBaseName the base name to use for the generated files
     * The report will be reportBaseName + ".html" in the temp file.
     * @throws IOException in case of problems writing the files
     */
    public DebuggingWebConnection(final WebConnection webConnection,
            final String reportBaseName) throws IOException {

        super(webConnection);

        wrappedWebConnection_ = webConnection;
        reportBaseName_ = reportBaseName;
        javaScriptFile_ = File.createTempFile(reportBaseName_, ".js");
        createOverview();
    }

    /**
     * Calls the wrapped webconnection and save the received response.
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequestSettings webRequestSettings) throws IOException {
        final WebResponse response = wrappedWebConnection_.getResponse(webRequestSettings);
        saveResponse(response, webRequestSettings);
        return response;
    }

    /**
     * Saves the response content in the temp dir and adds it to the summary page.
     * @param response the response to save
     * @param settings the settings used to get the response
     * @throws IOException if a problem occurs writing the file
     */
    protected void saveResponse(final WebResponse response, final WebRequestSettings settings)
        throws IOException {
        counter_++;
        final String extension;
        if ("application/x-javascript".equals(response.getContentType())) {
            extension = ".js";
        }
        else if ("text/html".equals(response.getContentType())) {
            extension = ".html";
        }
        else {
            extension = ".txt";
        }
        final File f = File.createTempFile(reportBaseName_ + counter_ + "-", extension);
        final String content = response.getContentAsString();
        FileUtils.writeStringToFile(f, content, response.getContentCharSet());
        LOG.info("Created file " + f.getAbsolutePath()
                + " for response " + counter_ + ": " + response.getUrl());

        final StringBuilder buffer = new StringBuilder();
        buffer.append("tab[tab.length] = {code: " + response.getStatusCode() + ", ");
        buffer.append("fileName: '" + f.getName() + "', ");
        buffer.append("contentType: '" + response.getContentType() + "', ");
        buffer.append("method: '" + settings.getHttpMethod().name() + "', ");
        buffer.append("url: '" + response.getUrl() + "', ");
        buffer.append("headers: " + nameValueListToJsMap(response.getResponseHeaders()));
        buffer.append("};\n");
        final FileWriter jsFileWriter = new FileWriter(javaScriptFile_, true);
        jsFileWriter.write(buffer.toString());

        jsFileWriter.close();
    }

    /**
     * Produces a String that will produce a JS map like "{'key1': 'value1', 'key 2': 'value2'}"
     * @param headers a list of {@link NameValuePair}
     * @return the JS String
     */
    static String nameValueListToJsMap(final List<NameValuePair> headers) {
        if (headers == null || headers.isEmpty()) {
            return "{}";
        }
        final StringBuilder buffer = new StringBuilder("{");
        for (final NameValuePair header : headers) {
            buffer.append("'" + header.getName() + "': '" + header.getValue().replaceAll("'", "\\'") + "', ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * Creates the summary file and the JavaScript file that will be updated for each received response
     * @throws IOException if a problem occurs writing the file
     */
    private void createOverview() throws IOException {
        FileUtils.writeStringToFile(javaScriptFile_, "var tab = [];\n", TextUtil.DEFAULT_CHARSET);

        final File summary = new File(javaScriptFile_.getParentFile(), reportBaseName_ + ".html");
        final String content = "<html><head><title>Summary for " + reportBaseName_ + "</title>\n"
            + "<h1>Received responses</h1>\n"
            + "<script src='" + javaScriptFile_.getName() + "' type='text/javascript'></script>\n"
            + "</head>\n"
            + "<body>"
            + "<ol>\n"
            + "<script>\n"
            + "for (var i=0; i<tab.length; i++) {\n"
            + "  var curRes = tab[i];\n"
            + "  document.writeln('<li>'"
            + " + curRes.code + ' ' + curRes.method + ' ' "
            + " + '<a href=\"' + curRes.fileName + '\" target=_blank>' + curRes.url + '</a> "
            + " (' + curRes.contentType + ')</li>');\n"
            + "}\n"
            + "</script>\n"
            + "</ol>"
            + "</body></html>";

        FileUtils.writeStringToFile(summary, content, TextUtil.DEFAULT_CHARSET);
        LOG.info("Summary will be in " + summary.getAbsolutePath());
    }
}
