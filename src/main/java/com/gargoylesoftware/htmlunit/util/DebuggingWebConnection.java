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
package com.gargoylesoftware.htmlunit.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Script;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebResponseImpl;

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
 * In this example an overview page will be generated under the name myTest/index.html in the temp directory
 * and all received responses will be saved int the myTest folder.<br>
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
    private final File javaScriptFile_;
    private final File reportFolder_;
    private boolean uncompressJavaScript_ = true;

    /**
     * Wraps a web connection to have a report generated of the received responses.
     * @param webConnection the webConnection that do the real work
     * @param dirName the name of the directory to create in the tmp folder to save received responses.
     * If this folder already exists, it will be deleted first.
     * @throws IOException in case of problems writing the files
     */
    public DebuggingWebConnection(final WebConnection webConnection,
            final String dirName) throws IOException {

        super(webConnection);

        wrappedWebConnection_ = webConnection;
        final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        reportFolder_ = new File(tmpDir, dirName);
        if (reportFolder_.exists()) {
            FileUtils.forceDelete(reportFolder_);
        }
        FileUtils.forceMkdir(reportFolder_);
        javaScriptFile_ = new File(reportFolder_, "hu.js");
        createOverview();
    }

    /**
     * Calls the wrapped webconnection and save the received response.
     * {@inheritDoc}
     */
    @Override
    public WebResponse getResponse(final WebRequestSettings settings) throws IOException {
        WebResponse response = wrappedWebConnection_.getResponse(settings);
        if (isUncompressJavaScript() && isJavaScript(response)) {
            response = uncompressJavaScript(response);
        }
        saveResponse(response, settings);
        return response;
    }

    /**
     * Tries to uncompress the JavaScript code in the provided response.
     * @param response the response to uncompress
     * @return a new response with uncompressed JavaScript code or the original response in case of failure
     */
    protected WebResponse uncompressJavaScript(final WebResponse response) {
        final WebRequestSettings requestSettings = response.getRequestSettings();
        final String scriptName = requestSettings.getUrl().toString();
        final String scriptSource = response.getContentAsString();

        // skip if it is already formatted? => TODO

        final ContextFactory factory = new ContextFactory();
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                cx.setOptimizationLevel(-1);
                final Script script = cx.compileString(scriptSource, scriptName, 0, null);
                return cx.decompileScript(script, 4);
            }
        };

        try {
            final String decompileScript = (String) factory.call(action);
            final WebResponseData wrd = new WebResponseData(decompileScript.getBytes(), response.getStatusCode(),
                response.getStatusMessage(), response.getResponseHeaders());
            return new WebResponseImpl(wrd, response.getRequestSettings().getUrl(),
                response.getRequestSettings().getHttpMethod(), response.getLoadTime());
        }
        catch (final Exception e) {
            LOG.warn("Failed to decompress JavaScript response. Delivering as it.", e);
        }

        return response;
    }

    /**
     * Adds a mark that will be visible in the HTML result page generated by this class.
     * @param mark the text
     * @throws IOException if a problem occurs writing the file
     */
    public void addMark(final String mark) throws IOException {
        appendToJSFile("tab[tab.length] = \"" + mark + "\";\n");
        LOG.info("--- " + mark + " ---");
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
        if (isJavaScript(response)) {
            extension = ".js";
        }
        else if ("text/html".equals(response.getContentType())) {
            extension = ".html";
        }
        else {
            extension = ".txt";
        }
        final File f = createFile(settings.getUrl(), extension);
        final String content = response.getContentAsString();
        final URL url = response.getRequestSettings().getUrl();
        FileUtils.writeStringToFile(f, content, response.getContentCharset());
        LOG.info("Created file " + f.getAbsolutePath() + " for response " + counter_ + ": " + url);

        final StringBuilder buffer = new StringBuilder();
        buffer.append("tab[tab.length] = {code: " + response.getStatusCode() + ", ");
        buffer.append("fileName: '" + f.getName() + "', ");
        buffer.append("contentType: '" + response.getContentType() + "', ");
        buffer.append("method: '" + settings.getHttpMethod().name() + "', ");
        if (settings.getHttpMethod() == HttpMethod.POST && settings.getEncodingType() == FormEncodingType.URL_ENCODED) {
            buffer.append("postParameters: " + nameValueListToJsMap(settings.getRequestParameters()) + ", ");
        }
        buffer.append("url: '" + url + "', ");
        buffer.append("loadTime: " + response.getLoadTime() + ", ");
        final byte[] bytes = response.getContentAsBytes();
        buffer.append("responseSize: " + ((bytes == null) ? 0 : bytes.length) + ", ");
        buffer.append("responseHeaders: " + nameValueListToJsMap(response.getResponseHeaders()));
        buffer.append("};\n");
        appendToJSFile(buffer.toString());
    }

    /**
     * Indicates if the response contains JavaScript content.
     * @param response the response to inspect
     * @return <code>false</code> if it is not recognized as JavaScript
     */
    protected boolean isJavaScript(final WebResponse response) {
        final String contentType = response.getContentType();
        return contentType.contains("javascript") || contentType.contains("ecmascript")
            || (contentType.startsWith("text/") && contentType.contains("js"));
    }

    /**
     * Indicates if it should try to format responses recognized as JavaScript.
     * @return default is <code>false</code> to deliver the original content
     */
    public boolean isUncompressJavaScript() {
        return uncompressJavaScript_;
    }

    /**
     * Indicates that responses recognized as JavaScript should be formatted or not.
     * Formatting is interesting for debugging when the original script is compressed on a single line.
     * It allows to better follow with a debugger and to obtain more interesting error messages.
     * @param decompress <code>true</code> if JavaScript responses should be uncompressed
     */
    public void setUncompressJavaScript(final boolean decompress) {
        uncompressJavaScript_ = decompress;
    }

    private void appendToJSFile(final String str) throws IOException {
        final FileWriter jsFileWriter = new FileWriter(javaScriptFile_, true);
        jsFileWriter.write(str);

        jsFileWriter.close();
    }

    /**
     * Computes the best file to save the response to the given URL.
     * @param url the requested URL
     * @param extension the preferred extension
     * @return the file to create
     * @throws IOException if a problem occurs creating the file
     */
    private File createFile(final URL url, final String extension) throws IOException {
        String name = url.getPath().replaceFirst("/$", "").replaceAll(".*/", "");
        name = StringUtils.substringBefore(name, "?"); // remove query
        name = StringUtils.substringBefore(name, ";"); // remove additional info
        if (!name.endsWith(extension)) {
            name += extension;
        }
        int counter = 0;
        while (true) {
            final String fileName;
            if (counter != 0) {
                fileName = StringUtils.substringBeforeLast(name, ".")
                    + "_" + counter + "." + StringUtils.substringAfterLast(name, ".");
            }
            else {
                fileName = name;
            }
            final File f = new File(reportFolder_, fileName);
            if (f.createNewFile()) {
                return f;
            }
            counter++;
        }
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

        final URL indexResource = DebuggingWebConnection.class.getResource("DebuggingWebConnection.index.html");
        if (indexResource == null) {
            throw new RuntimeException("Missing dependency DebuggingWebConnection.index.html");
        }
        final File summary = new File(reportFolder_, "index.html");
        FileUtils.copyURLToFile(indexResource, summary);

        LOG.info("Summary will be in " + summary.getAbsolutePath());
    }
}
