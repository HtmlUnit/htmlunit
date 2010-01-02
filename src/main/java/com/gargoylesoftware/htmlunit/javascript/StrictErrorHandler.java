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
package com.gargoylesoftware.htmlunit.javascript;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An error handler that throws an exception for all problems no matter how minor.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StrictErrorHandler implements ErrorHandler {
    /**
     * A warning occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void warning(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }

    /**
     * An error occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void error(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }

    /**
     * A fatal error occurred.
     *
     * @param rethrow an exception which will be rethrown
     * @throws SAXParseException always
     */
    public void fatalError(final SAXParseException rethrow) throws SAXParseException {
        throw rethrow;
    }
}
