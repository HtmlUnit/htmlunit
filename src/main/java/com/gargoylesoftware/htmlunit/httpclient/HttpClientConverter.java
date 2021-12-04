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
package com.gargoylesoftware.htmlunit.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.message.BasicNameValuePair;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Helper methods to convert from/to HttpClient.
 *
 * @author Ronald Brill
 * @author Joerg Werner
 */
public final class HttpClientConverter {

    private HttpClientConverter() {
    }

    /**
     * Converts the specified name/value pairs into HttpClient name/value pairs.
     * @param pairs the name/value pairs to convert
     * @return the converted name/value pairs
     */
    public static List<org.apache.hc.core5.http.NameValuePair>
                        nameValuePairsToHttpClient(final List<NameValuePair> pairs) {
        final List<org.apache.hc.core5.http.NameValuePair> resultingPairs = new ArrayList<>(pairs.size());
        for (final NameValuePair pair : pairs) {
            resultingPairs.add(new BasicNameValuePair(pair.getName(), pair.getValue()));
        }
        return resultingPairs;
    }
}
