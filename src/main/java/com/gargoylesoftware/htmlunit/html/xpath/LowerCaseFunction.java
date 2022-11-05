/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Locale;

import javax.xml.transform.TransformerException;

import net.sourceforge.htmlunit.xpath.XPathContext;
import net.sourceforge.htmlunit.xpath.functions.FunctionDef1Arg;
import net.sourceforge.htmlunit.xpath.objects.XObject;
import net.sourceforge.htmlunit.xpath.objects.XString;

/**
 * Custom XPath function to convert the argument to lower case (using the {@link Locale#ROOT})
 * as in {@link java.lang.String#toLowerCase()}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class LowerCaseFunction extends FunctionDef1Arg {

    /**
     * {@inheritDoc}
     */
    @Override
    public XObject execute(final XPathContext xctxt) throws TransformerException {
        return new XString(getArg0AsString(xctxt).str().toLowerCase(Locale.ROOT));
    }
}
