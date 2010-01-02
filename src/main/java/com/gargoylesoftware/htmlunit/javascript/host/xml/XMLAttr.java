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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.apache.commons.lang.StringEscapeUtils;

import com.gargoylesoftware.htmlunit.javascript.host.Attr;

/**
 * A JavaScript object for an Attribute of XMLElement.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762738.aspx">MSDN documentation</a>
 * @version $Revision$
 * @author Sudhan Moghe
 */
public class XMLAttr extends Attr {

    private static final long serialVersionUID = -9062357417620766444L;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLAttr() { }

    /**
     * Returns the text of this attribute.
     * @return the value of this attribute
     */
    public String jsxGet_text() {
        return jsxGet_value();
    }

    /**
     * Sets the text of this attribute.
     * @param value the new value of this attribute
     */
    public void jsxSet_text(final String value) {
        jsxSet_value(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_xml() {
        final StringBuilder sb = new StringBuilder();
        sb.append(jsxGet_name());
        sb.append('=');
        sb.append('"');
        sb.append(StringEscapeUtils.escapeXml(jsxGet_value()));
        sb.append('"');
        return sb.toString();
    }
}
