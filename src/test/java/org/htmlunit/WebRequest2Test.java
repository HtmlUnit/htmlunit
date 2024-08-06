/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang3.StringUtils;
import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.BrowserParameterizedRunner.Default;
import org.htmlunit.util.NameValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for {@link WebRequest#getParameters()}.
 * This method is used by Spring test to shortcut the
 * request processing (without involving a server). Therefore
 * we have to make sure this works as expected in all cases.
 *
 * see https://github.com/spring-projects/spring-framework/issues/28240
 * see https://github.com/HtmlUnit/htmlunit/pull/836
 *
 * @author Ronald Brill
 * @author Kristof Neirynck
 */
@RunWith(BrowserParameterizedRunner.class)
public class WebRequest2Test extends WebServerTestCase {

    public static class TestParameters {
        private final String label_;
        private final List<NameValuePair> pairs_;

        TestParameters(final String label, final List<NameValuePair> pairs) {
            label_ = label;
            pairs_ = pairs;
        }

        @Override
        public String toString() {
            return label_;
        }

        public List<NameValuePair> getPairs() {
            return pairs_;
        }
    }

    /**
     * Performs pre-test construction.
     * @throws Exception if an error occurs
     */
    @Before
    public void setup() throws Exception {
        // we have to stop all servers running already to free the port
        WebDriverTestCase.stopWebServers();

        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/", InspectServlet.class);
        startWebServer("./", null, servlets);
    }

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> data = new ArrayList<>();

        final HttpMethod[] methods = {HttpMethod.OPTIONS, HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST,
                                      HttpMethod.PUT, HttpMethod.DELETE, /*HttpMethod.TRACE,*/ HttpMethod.PATCH};
        final String[] queries = {"", "?a=b", "?a=b&c=d", "?a=", "?a", "?", "?a=b&a=d"};
        final FormEncodingType[] encodings =
            {FormEncodingType.URL_ENCODED, FormEncodingType.MULTIPART, FormEncodingType.TEXT_PLAIN};

        List<NameValuePair> parameterPairs = new ArrayList<>();
        final TestParameters emptyParameters = new TestParameters("empty", parameterPairs);

        parameterPairs = new ArrayList<>();
        parameterPairs.add(new NameValuePair("p1", "v1"));
        final TestParameters oneParameter = new TestParameters("oneParameter", parameterPairs);

        parameterPairs = new ArrayList<>();
        parameterPairs.add(new NameValuePair("a", ""));
        final TestParameters emptyValueParameter = new TestParameters("emptyValue", parameterPairs);

        parameterPairs = new ArrayList<>();
        parameterPairs.add(new NameValuePair("a", "b"));
        final TestParameters sameAsInQueryParameter = new TestParameters("sameAsInQuery", parameterPairs);

        parameterPairs = new ArrayList<>();
        parameterPairs.add(new NameValuePair("a", "other"));
        final TestParameters sameKeyAsInQueryParameter = new TestParameters("sameKeyAsInQuery", parameterPairs);

        parameterPairs = new ArrayList<>();
        parameterPairs.add(new NameValuePair("same", "value1"));
        parameterPairs.add(new NameValuePair("same", "value2"));
        final TestParameters sameKeyDifferentValuesParameter
                = new TestParameters("sameKeyDifferentValues", parameterPairs);

        final TestParameters[] parameters =
            {null, emptyParameters, oneParameter, emptyValueParameter, sameAsInQueryParameter,
                sameKeyAsInQueryParameter, sameKeyDifferentValuesParameter};

        final String[] bodies = {"null", "", "a=b", "a=b&c=d", "a=", "a", "a=b&a=d"};

        for (final HttpMethod method : methods) {
            for (final String query : queries) {
                for (final FormEncodingType encoding : encodings) {
                    for (final TestParameters parameter : parameters) {
                        for (final String body : bodies) {
                            data.add(new Object[] {method, query, encoding, parameter, body});
                        }
                    }
                }
            }
        }

        return data;
    }

    /**
     * The HttpMethod.
     */
    @Parameter
    public HttpMethod httpMethod_;

    /**
     * The query.
     */
    @Parameter(1)
    public String query_;

    /**
     * The FormEncodingType.
     */
    @Parameter(2)
    public FormEncodingType encoding_;

    /**
     * The FormEncodingType.
     */
    @Parameter(3)
    public TestParameters parameter_;

    /**
     * The body.
     */
    @Parameter(4)
    public String body_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void test() throws Exception {
        final URL url = new URL(URL_FIRST, query_);
        final WebRequest request = new WebRequest(url);
        request.setHttpMethod(httpMethod_);
        request.setEncodingType(encoding_);

        if ("null".equals(body_)) {
            if (parameter_ != null) {
                request.setRequestParameters(parameter_.getPairs());
            }
        }
        else {
            if (httpMethod_ == HttpMethod.POST
                    || httpMethod_ == HttpMethod.PUT
                    || httpMethod_ == HttpMethod.PATCH
                    || httpMethod_ == HttpMethod.DELETE
                    || httpMethod_ == HttpMethod.OPTIONS) {
                request.setRequestBody(body_);
            }
        }

        final WebResponse response = getWebClient().getWebConnection().getResponse(request);

        // calculate expectation from bounce servlet
        String expectedContent = response.getContentAsString();

        if (HttpMethod.HEAD.equals(request.getHttpMethod())) {
            assertEquals(0, expectedContent.length());
            expectedContent = InspectServlet.PARAMETERS_;
        }

        assertTrue(expectedContent.startsWith("Parameters: \n"));
        expectedContent = expectedContent.substring("Parameters: \n".length()).trim();

        final List<NameValuePair> expectedParameters = new ArrayList<>();
        if (!StringUtils.isAllBlank(expectedContent)) {
            for (final String line : expectedContent.split("\n")) {
                final String[] parts = line.split(":");
                assertEquals(2, parts.length);

                String name = parts[0].trim();
                name = StringUtils.strip(name, "'");

                String values = parts[1].trim();
                values = StringUtils.strip(values, "[]");

                for (String value : values.split(",")) {
                    value = value.trim();
                    value = StringUtils.strip(value, "'");

                    final NameValuePair pair = new NameValuePair(name, value);
                    expectedParameters.add(pair);
                }
            }
        }

        final List<String> expectedNames = expectedParameters.stream()
                                                .map(NameValuePair::getName)
                                                .distinct()
                                                .collect(Collectors.toList());

        final List<NameValuePair> parameters = request.getParameters();
        final List<String> parameterNames = parameters.stream()
                                                .map(NameValuePair::getName)
                                                .distinct()
                                                .collect(Collectors.toList());

        assertEquals("Parameter names should match", expectedNames, parameterNames);

        // we can't compare directly because the servlet api collects by name
        // this checks for the same values in the same order
        for (final String name : expectedNames) {
            final List<String> expectedValues = expectedParameters.stream()
                                                    .filter(pair -> name.equals(pair.getName()))
                                                    .map(NameValuePair::getValue)
                                                    .collect(Collectors.toList());
            final List<String> values = parameters.stream()
                                                    .filter(pair -> name.equals(pair.getName()))
                                                    .map(NameValuePair::getValue)
                                                    .collect(Collectors.toList());
            assertEquals("Parameter values for parameter with name '" + name + "' should match",
                    expectedValues, values);
        }
    }

    /**
     * Servlet.
     */
    public static class InspectServlet extends HttpServlet {

        /** Hack. */
        public static String PARAMETERS_;

        @Override
        protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            if ("patch".equalsIgnoreCase(req.getMethod())) {
                doPatch(req, resp);
                return;
            }

            super.service(req, resp);
        }

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doHead(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            // head does not deliver a body
            bounceToStatic(req, resp);
        }

        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doPut(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doOptions(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        @Override
        protected void doTrace(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        protected void doPatch(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            bounce(req, resp);
        }

        private static void bounce(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            try (Writer writer = resp.getWriter()) {
                bounce(writer, req, resp);
            }
        }

        private static void bounceToStatic(final HttpServletRequest req, final HttpServletResponse resp)
                throws IOException {
            try (StringWriter writer = new StringWriter()) {
                bounce(writer, req, resp);
                PARAMETERS_ = writer.toString();
            }
        }

        private static void bounce(final Writer writer,
                final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
            writer.write("Parameters: \n");

            // use only getParameterMap() here because we like to have the same behavior
            for (final Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                if (entry.getValue() == null) {
                    writer.write("  '" + entry.getKey() + "': [null]\n");
                }
                else if (entry.getValue().length == 0) {
                    writer.write("  '" + entry.getKey() + "': []\n");
                }
                else {
                    writer.write("  '" + entry.getKey() + "': [");
                    boolean first = true;
                    for (final String val : entry.getValue()) {
                        if (first) {
                            writer.write("'" + val + "'");
                            first = false;
                        }
                        else {
                            writer.write(", '" + val + "'");
                        }
                    }
                    writer.write("]\n");
                }
            }

            // different from the servlet api spring also uses the file upload information
            try {
                // if (ServletFileUpload.isMultipartContent(req)) {
                // ignore the post request check
                if (FileUploadBase.isMultipartContent(new ServletRequestContext(req))) {
                    final DiskFileItemFactory factory = new DiskFileItemFactory();

                    final ServletFileUpload upload = new ServletFileUpload(factory);
                    final List<FileItem> items = upload.parseRequest(req);
                    for (final FileItem fileItem : items) {
                        writer.write("  '" + fileItem.getFieldName() + "': '" + fileItem.getString() + "'\n");
                    }
                }
            }
            catch (final FileUploadException e) {
                throw new IOException(e);
            }
        }
    }
}
