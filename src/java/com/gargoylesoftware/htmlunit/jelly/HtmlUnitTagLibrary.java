/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import org.apache.commons.jelly.TagLibrary;

/**
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlUnitTagLibrary extends TagLibrary {

    /**
     * Create a new instance
     */
    public HtmlUnitTagLibrary() {
        registerTag("webClient", WebClientTag.class);
        registerTag("getPage", GetPageTag.class);
        registerTag("assertStatusCode", AssertStatusCodeTag.class);
        registerTag("assertContentType", AssertContentTypeTag.class);
        registerTag("assertTitle", AssertTitleTag.class);
    }
}

