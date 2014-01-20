/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;

/**
 * Tests for {@link MSXMLActiveXObjectFactory}.
 *
 * @version $Revision$
 * @author Frank Danek
 */
public class MSXMLActiveXObjectFactoryTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void supportsXMLDOMDocument() throws Exception {
        assertFalse(MSXMLActiveXObjectFactory.isXMLDOMDocument(null));
        assertFalse(MSXMLActiveXObjectFactory.isXMLDOMDocument("foo"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Microsoft.XmlDom"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("MSXML2.DOMDocument"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.DOMDocument.3.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.DOMDocument.4.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.DOMDocument.5.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.DOMDocument.6.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("MSXML2.FreeThreadedDOMDocument"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.FreeThreadedDOMDocument.3.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.FreeThreadedDOMDocument.4.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.FreeThreadedDOMDocument.5.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLDOMDocument("Msxml2.FreeThreadedDOMDocument.6.0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void supportsXMLHTTPRequest() throws Exception {
        assertFalse(MSXMLActiveXObjectFactory.isXMLHTTPRequest(null));
        assertFalse(MSXMLActiveXObjectFactory.isXMLHTTPRequest("foo"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Microsoft.XMLHTTP"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Msxml2.XMLHTTP"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Msxml2.XMLHTTP.3.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Msxml2.XMLHTTP.4.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Msxml2.XMLHTTP.5.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXMLHTTPRequest("Msxml2.XMLHTTP.6.0"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void supportsXSLTemplate() throws Exception {
        assertFalse(MSXMLActiveXObjectFactory.isXSLTemplate(null));
        assertFalse(MSXMLActiveXObjectFactory.isXSLTemplate("foo"));
        assertTrue(MSXMLActiveXObjectFactory.isXSLTemplate("Msxml2.XSLTemplate"));
        assertTrue(MSXMLActiveXObjectFactory.isXSLTemplate("Msxml2.XSLTemplate.3.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXSLTemplate("Msxml2.XSLTemplate.4.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXSLTemplate("Msxml2.XSLTemplate.5.0"));
        assertTrue(MSXMLActiveXObjectFactory.isXSLTemplate("Msxml2.XSLTemplate.6.0"));
    }
}
