/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.applets.AppletClassLoader;
import com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Wrapper for the HTML element "applet".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class HtmlApplet extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "applet";

    private Applet applet_;
    private AppletClassLoader appletClassLoader_;

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlApplet(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Returns the value of the attribute "codebase". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "codebase" or an empty string if that attribute isn't defined
     */
    public final String getCodebaseAttribute() {
        return getAttribute("codebase");
    }

    /**
     * Returns the value of the attribute "archive". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "archive" or an empty string if that attribute isn't defined
     */
    public final String getArchiveAttribute() {
        return getAttribute("archive");
    }

    /**
     * Returns the value of the attribute "code". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "code" or an empty string if that attribute isn't defined
     */
    public final String getCodeAttribute() {
        return getAttribute("code");
    }

    /**
     * Returns the value of the attribute "object". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "object" or an empty string if that attribute isn't defined
     */
    public final String getObjectAttribute() {
        return getAttribute("object");
    }

    /**
     * Returns the value of the attribute "alt". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alt" or an empty string if that attribute isn't defined
     */
    public final String getAltAttribute() {
        return getAttribute("alt");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute "width". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "width" or an empty string if that attribute isn't defined
     */
    public final String getWidthAttribute() {
        return getAttribute("width");
    }

    /**
     * Returns the value of the attribute "height". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "height" or an empty string if that attribute isn't defined
     */
    public final String getHeightAttribute() {
        return getAttribute("height");
    }

    /**
     * Returns the value of the attribute "align". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "align" or an empty string if that attribute isn't defined
     */
    public final String getAlignAttribute() {
        return getAttribute("align");
    }

    /**
     * Returns the value of the attribute "hspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hspace" or an empty string if that attribute isn't defined
     */
    public final String getHspaceAttribute() {
        return getAttribute("hspace");
    }

    /**
     * Returns the value of the attribute "vspace". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vspace" or an empty string if that attribute isn't defined
     */
    public final String getVspaceAttribute() {
        return getAttribute("vspace");
    }

    /**
     * Gets the applet referenced by this tag. Instantiates it if necessary.
     * @return the applet
     * @throws IOException in case of problem
     */
    public Applet getApplet() throws IOException {
        setupAppletIfNeeded();
        return applet_;
    }

    /**
     * Downloads the associated content specified in the code attribute.
     *
     * @throws IOException if an error occurs while downloading the content
     */
    @SuppressWarnings("unchecked")
    private synchronized void setupAppletIfNeeded() throws IOException {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", getNameAttribute());

        params.put("object", getObjectAttribute());
        params.put("align", getAlignAttribute());
        params.put("alt", getAltAttribute());
        params.put("height", getHeightAttribute());
        params.put("hspace", getHspaceAttribute());
        params.put("vspace", getVspaceAttribute());
        params.put("width", getWidthAttribute());

        final DomNodeList<HtmlElement> paramTags = getElementsByTagName("param");
        for (HtmlElement paramTag : paramTags) {
            final HtmlParameter parameter = (HtmlParameter) paramTag;
            params.put(parameter.getNameAttribute(), parameter.getValueAttribute());
        }

        String codebaseProperty = getCodebaseAttribute();
        if (StringUtils.isNotEmpty(codebaseProperty)) {
            params.put("codebase", codebaseProperty);
        }
        codebaseProperty = params.get("codebase");

        String archiveProperty = getArchiveAttribute();
        if (StringUtils.isNotEmpty(archiveProperty)) {
            params.put("archive", archiveProperty);
        }
        archiveProperty = params.get("archive");

        if (null == applet_) {
            final HtmlPage page = (HtmlPage) getPage();
            final WebClient webclient = page.getWebClient();

            String appletClassName = getCodeAttribute();
            if (appletClassName.endsWith(".class")) {
                appletClassName = appletClassName.substring(0, appletClassName.length() - 6);
            }

            appletClassLoader_ = new AppletClassLoader();

            final String documentUrl = page.getWebResponse().getWebRequest().getUrl().toExternalForm();
            String baseUrl = documentUrl;
            if (StringUtils.isNotEmpty(getCodebaseAttribute())) {
                // codebase can be relative to the page
                baseUrl = UrlUtils.resolveUrl(baseUrl, getCodebaseAttribute());
            }
            if (!baseUrl.endsWith("/")) {
                baseUrl = baseUrl + "/";
            }

            // check archive
            final String[] archives = StringUtils.split(archiveProperty, ',');
            if (null != archives) {
                for (int i = 0; i < archives.length; i++) {
                    String tmpArchive = archives[i].trim();
                    final String tempUrl = UrlUtils.resolveUrl(baseUrl, tmpArchive);
                    final URL archiveUrl = UrlUtils.toUrlUnsafe(tempUrl);

                    WebResponse response = webclient.loadWebResponse(new WebRequest(archiveUrl));
                    webclient.throwFailingHttpStatusCodeExceptionIfNecessary(response);
                    appletClassLoader_.addArchiveToClassPath(response);
                }
            }

            // no archive attribute, single class
            if (null == archives || archives.length == 0)  {
                final String tempUrl = UrlUtils.resolveUrl(baseUrl, getCodeAttribute());
                final URL classUrl = UrlUtils.toUrlUnsafe(tempUrl);

                WebResponse response = webclient.loadWebResponse(new WebRequest(classUrl));
                webclient.throwFailingHttpStatusCodeExceptionIfNecessary(response);
                appletClassLoader_.addClassToClassPath(appletClassName, response);
            }

            try {
                final Class<Applet> appletClass = (Class<Applet>) appletClassLoader_.loadClass(appletClassName);
                applet_ = appletClass.newInstance();
                applet_.setStub(new AppletStubImpl(this, params,
                        UrlUtils.toUrlUnsafe(baseUrl), UrlUtils.toUrlUnsafe(documentUrl)));
                applet_.init();
                applet_.start();
            }
            catch (final ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            catch (final InstantiationException e) {
                throw new RuntimeException(e);
            }
            catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
