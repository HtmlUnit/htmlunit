/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import java.util.Map;
import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;

/**
 * A container for all the JavaScript configuration information.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public final class MSXMLConfiguration extends AbstractJavaScriptConfiguration {

    @SuppressWarnings("unchecked")
    static final Class<MSXMLScriptable>[] CLASSES_ = new Class[] {
        XMLDOMAttribute.class, XMLDOMCDATASection.class, XMLDOMCharacterData.class, XMLDOMComment.class,
        XMLDOMDocument.class, XMLDOMDocumentFragment.class, XMLDOMDocumentType.class, XMLDOMElement.class,
        XMLDOMImplementation.class, XMLDOMNamedNodeMap.class, XMLDOMNode.class, XMLDOMNodeList.class,
        XMLDOMParseError.class, XMLDOMProcessingInstruction.class, XMLDOMSelection.class, XMLDOMText.class,
        XMLHTTPRequest.class, XSLProcessor.class, XSLTemplate.class
    };

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<BrowserVersion, MSXMLConfiguration> CONFIGURATION_MAP_ =
        new WeakHashMap<BrowserVersion, MSXMLConfiguration>();

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private MSXMLConfiguration(final BrowserVersion browser) {
        super(browser);
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multi-threaded access to the JavaScript configuration.
     * @param browserVersion the {@link BrowserVersion}
     * @return the instance for the specified {@link BrowserVersion}
     */
    public static synchronized MSXMLConfiguration getInstance(final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            throw new IllegalStateException("BrowserVersion must be defined");
        }
        MSXMLConfiguration configuration = CONFIGURATION_MAP_.get(browserVersion);

        if (configuration == null) {
            configuration = new MSXMLConfiguration(browserVersion);
            CONFIGURATION_MAP_.put(browserVersion, configuration);
        }
        return configuration;
    }

    @Override
    protected Class<MSXMLScriptable>[] getClasses() {
        return CLASSES_;
    }
}
