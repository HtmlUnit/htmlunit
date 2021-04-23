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
package com.gargoylesoftware.htmlunit.html;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AppletConfirmHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.applets.AppletClassLoader;
import com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Wrapper for the HTML element "object".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlObject extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlObject.class);

    private static final String APPLET_TYPE = "application/x-java-applet";
    private static final String ARCHIVE = "archive";
    private static final String CACHE_ARCHIVE = "cache_archive";
    private static final String CODEBASE = "codebase";

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "object";

    private Applet applet_;

    /**
     * Creates an instance of HtmlObject
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlObject(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute {@code declare}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code declare}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDeclareAttribute() {
        return getAttributeDirect("declare");
    }

    /**
     * Returns the value of the attribute {@code classid}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code classid}
     * or an empty string if that attribute isn't defined.
     */
    public final String getClassIdAttribute() {
        return getAttributeDirect("classid");
    }

    /**
     * Returns the value of the attribute "codebase". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codebase"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodebaseAttribute() {
        return getAttributeDirect("codebase");
    }

    /**
     * Returns the value of the attribute {@code data}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code data}
     * or an empty string if that attribute isn't defined.
     */
    public final String getDataAttribute() {
        return getAttributeDirect("data");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTypeAttribute() {
        return getAttributeDirect("type");
    }

    /**
     * Returns the value of the attribute "codetype". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codetype"
     * or an empty string if that attribute isn't defined.
     */
    public final String getCodeTypeAttribute() {
        return getAttributeDirect("codetype");
    }

    /**
     * Returns the value of the attribute {@code archive}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code archive}
     * or an empty string if that attribute isn't defined.
     */
    public final String getArchiveAttribute() {
        return getAttributeDirect("archive");
    }

    /**
     * Returns the value of the attribute {@code standby}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code standby}
     * or an empty string if that attribute isn't defined.
     */
    public final String getStandbyAttribute() {
        return getAttributeDirect("standby");
    }

    /**
     * Returns the value of the attribute {@code height}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code height}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHeightAttribute() {
        return getAttributeDirect("height");
    }

    /**
     * Returns the value of the attribute {@code width}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code width}
     * or an empty string if that attribute isn't defined.
     */
    public final String getWidthAttribute() {
        return getAttributeDirect("width");
    }

    /**
     * Returns the value of the attribute {@code usemap}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code usemap}
     * or an empty string if that attribute isn't defined.
     */
    public final String getUseMapAttribute() {
        return getAttributeDirect("usemap");
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name}
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex}
     * or an empty string if that attribute isn't defined.
     */
    public final String getTabIndexAttribute() {
        return getAttributeDirect("tabindex");
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align}
     * or an empty string if that attribute isn't defined.
     */
    public final String getAlignAttribute() {
        return getAttributeDirect("align");
    }

    /**
     * Returns the value of the attribute {@code border}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code border}
     * or an empty string if that attribute isn't defined.
     */
    public final String getBorderAttribute() {
        return getAttributeDirect("border");
    }

    /**
     * Returns the value of the attribute {@code hspace}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getHspaceAttribute() {
        return getAttributeDirect("hspace");
    }

    /**
     * Returns the value of the attribute {@code vspace}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code vspace}
     * or an empty string if that attribute isn't defined.
     */
    public final String getVspaceAttribute() {
        return getAttributeDirect("vspace");
    }

    /**
     * Initialize the ActiveX(Mock).
     * {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        if (getOwnerDocument() instanceof XmlPage) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Object node added: " + asXml());
        }

        final String clsId = getClassIdAttribute();
        if (ATTRIBUTE_NOT_DEFINED != clsId && getPage().getWebClient().isJavaScriptEngineEnabled()) {
            ((HTMLObjectElement) getScriptableObject()).setClassid(clsId);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }

    /**
     * Gets the applet referenced by this tag. Instantiates it if necessary.
     *
     * @return the applet or null, if the installed AppletConfirmHandler
     * prohibits this applet
     * @throws IOException in case of problem
     */
    public Applet getApplet() throws IOException {
        setupAppletIfNeeded();
        return applet_;
    }

    /**
     * Download the associated content specified in the code attribute.
     *
     * @throws IOException if an error occurs while downloading the content
     */
    @SuppressWarnings("unchecked")
    private synchronized void setupAppletIfNeeded() throws IOException {
        if (applet_ != null) {
            return;
        }

        if (StringUtils.isBlank(getTypeAttribute()) || !getTypeAttribute().startsWith(APPLET_TYPE)) {
            return;
        }

        final HashMap<String, String> params = new HashMap<>();
        params.put("name", getNameAttribute());

        params.put("height", getHeightAttribute());
        params.put("width", getWidthAttribute());

        final DomNodeList<HtmlElement> paramTags = getElementsByTagName("param");
        for (final HtmlElement paramTag : paramTags) {
            final HtmlParameter parameter = (HtmlParameter) paramTag;
            params.put(parameter.getNameAttribute(), parameter.getValueAttribute());
        }

        if (StringUtils.isEmpty(params.get(CODEBASE)) && StringUtils.isNotEmpty(getCodebaseAttribute())) {
            params.put(CODEBASE, getCodebaseAttribute());
        }
        final String codebaseProperty = params.get(CODEBASE);

        if (StringUtils.isEmpty(params.get(ARCHIVE)) && StringUtils.isNotEmpty(getArchiveAttribute())) {
            params.put(ARCHIVE, getArchiveAttribute());
        }

        final HtmlPage page = (HtmlPage) getPage();
        final WebClient webclient = page.getWebClient();

        final AppletConfirmHandler handler = webclient.getAppletConfirmHandler();
        if (null != handler && !handler.confirm(this)) {
            return;
        }

        String appletClassName = getAttributeDirect("code");
        if (StringUtils.isEmpty(appletClassName)) {
            appletClassName = params.get("code");
        }
        if (appletClassName.endsWith(".class")) {
            appletClassName = appletClassName.substring(0, appletClassName.length() - 6);
        }

        try (AppletClassLoader appletClassLoader =
                new AppletClassLoader((Window) getPage().getEnclosingWindow().getScriptableObject(),
                                            Thread.currentThread().getContextClassLoader())) {

            final String documentUrl = page.getUrl().toExternalForm();
            String baseUrl = UrlUtils.resolveUrl(documentUrl, ".");
            if (StringUtils.isNotEmpty(codebaseProperty)) {
                // codebase can be relative to the page
                baseUrl = UrlUtils.resolveUrl(baseUrl, codebaseProperty);
            }
            if (!baseUrl.endsWith("/")) {
                baseUrl = baseUrl + "/";
            }

            // check archive
            List<URL> archiveUrls = new LinkedList<>();
            String[] archives = StringUtils.split(params.get(ARCHIVE), ',');
            if (null != archives) {
                for (final String tmpArchive : archives) {
                    final String tempUrl = UrlUtils.resolveUrl(baseUrl, tmpArchive.trim());
                    final URL archiveUrl = UrlUtils.toUrlUnsafe(tempUrl);

                    appletClassLoader.addArchiveToClassPath(archiveUrl);
                    archiveUrls.add(archiveUrl);
                }
            }
            archives = StringUtils.split(params.get(CACHE_ARCHIVE), ',');
            if (null != archives) {
                for (final String tmpArchive : archives) {
                    final String tempUrl = UrlUtils.resolveUrl(baseUrl, tmpArchive.trim());
                    final URL archiveUrl = UrlUtils.toUrlUnsafe(tempUrl);

                    appletClassLoader.addArchiveToClassPath(archiveUrl);
                    archiveUrls.add(archiveUrl);
                }
            }
            archiveUrls = Collections.unmodifiableList(archiveUrls);

            // no archive attribute, single class
            if (archiveUrls.isEmpty()) {
                final String tempUrl = UrlUtils.resolveUrl(baseUrl, getAttributeDirect("code"));
                final URL classUrl = UrlUtils.toUrlUnsafe(tempUrl);

                final WebResponse response = webclient.loadWebResponse(new WebRequest(classUrl));
                try {
                    webclient.throwFailingHttpStatusCodeExceptionIfNecessary(response);
                    appletClassLoader.addClassToClassPath(appletClassName, response);
                }
                catch (final FailingHttpStatusCodeException e) {
                    // that is what the browser does, the applet only fails, if
                    // the main class is not loadable
                    LOG.error(e.getMessage(), e);
                }
            }

            try {
                final Class<Applet> appletClass = (Class<Applet>) appletClassLoader.loadClass(appletClassName);
                applet_ = appletClass.newInstance();
                applet_.setStub(new AppletStubImpl(getHtmlPageOrNull(), params,
                        UrlUtils.toUrlUnsafe(baseUrl), UrlUtils.toUrlUnsafe(documentUrl)));
                applet_.init();
                applet_.start();
            }
            catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("Loading applet '" + appletClassName + "' failed\n"
                            + "    " + e.toString()
                            + "\n    Classpath:\n" + appletClassLoader.info());
                }
                throw new RuntimeException(e);
            }
        }
    }
}
