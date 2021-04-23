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
package com.gargoylesoftware.htmlunit.attachment;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * An attachment represents a page received from the server which contains a
 * {@code Content-Disposition=attachment} header.
 *
 * @author Bruce Chapman
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class Attachment {

    /** The attached page. */
    private final Page page_;

    /**
     * Creates a new attachment for the specified page.
     * @param page the attached page
     */
    public Attachment(final Page page) {
        page_ = page;
    }

    /**
     * Returns the attached page.
     * @return the attached page
     */
    public Page getPage() {
        return page_;
    }

    /**
     * Returns the attachment's filename, as suggested by the <tt>Content-Disposition</tt>
     * header, or {@code null} if no filename was suggested.
     * @return the attachment's suggested filename, or {@code null} if none was suggested
     */
    public String getSuggestedFilename() {
        final WebResponse response = page_.getWebResponse();
        final String disp = response.getResponseHeaderValue(HttpHeader.CONTENT_DISPOSITION);
        int start = disp.indexOf("filename=");
        if (start == -1) {
            return null;
        }
        start += "filename=".length();
        if (start >= disp.length()) {
            return null;
        }

        int end = disp.indexOf(';', start);
        if (end == -1) {
            end = disp.length();
        }
        if (disp.charAt(start) == '"' && disp.charAt(end - 1) == '"') {
            start++;
            end--;
        }
        return disp.substring(start, end);
    }
}
