/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.KeyValuePair;

/**
 *  An element that can have it's values sent to the server during a form submit
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface SubmittableElement {
    /**
     *  Return an array of KeyValuePairs that are the values that will be sent
     *  back to the server whenever the current form is submitted .<p>
     *
     *  THIS METHOD IS INTENDED FOR THE USE OF THE FRAMEWORK ONLY AND SHOULD NOT
     *  BE USED BY CONSUMERS OF HTMLUNIT. USE AT YOUR OWN RISK.
     *
     * @return  See above
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    KeyValuePair[] getSubmitKeyValuePairs()
        throws ElementNotFoundException;
}

