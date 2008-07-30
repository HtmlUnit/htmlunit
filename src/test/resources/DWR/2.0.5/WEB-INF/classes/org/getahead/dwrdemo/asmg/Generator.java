/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.getahead.dwrdemo.asmg;

import java.util.StringTokenizer;

import org.directwebremoting.Security;

/**
 * Generate an anti-spam mailto link from an email address.
 * The output link looks something like this (where $1 is the username part of
 * the address and $2 is the hostname part:
 * <pre>
 * Contact us using:
 * &lt;script type="text/javascript"&gt;
 * var a = $1 + "@" + $2;
 * document.write("&lt;a href='mail" + "to:" + a + "'&gt;" + a + "&lt;/a&gt;");
 * &lt;/script&gt;
 * &lt;noscript&gt;[$1 at $2]&lt;/noscript&gt;
 * </pre>
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Generator
{
    /**
     * Generate an anti-spam mailto link from an email address
     * @param name The person to contact
     * @param email The address to generate a link from
     * @return The HTML snippet
     */
    public String generateAntiSpamMailto(String name, String email)
    {
        StringTokenizer st = new StringTokenizer(email, "@");
        if (Security.containsXssRiskyCharacters(email) || st.countTokens() != 2)
        {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }

        String before = st.nextToken();
        String after = st.nextToken();

        StringBuffer buffer = new StringBuffer();

        buffer.append("Contact ");
        buffer.append(Security.replaceXmlCharacters(name));
        buffer.append(" using: <span id=\"asmgLink\"></span>\n");
        buffer.append("<script type='text/javascript'>\n");

        buffer.append("var before = '");
        buffer.append(before);
        buffer.append("';\n");

        buffer.append("var after = '");
        buffer.append(after);
        buffer.append("';\n");

        buffer.append("var link = \"<a href='mail\" + \"to:\" + before + '@' + after + \"'>\" + before + '@' + after + \"</a>\";\n");

        buffer.append("document.getElementById(\"asmgLink\").innerHTML = link;\n");
        buffer.append("</script>\n");
        buffer.append("<noscript>[");
        buffer.append(before);
        buffer.append(" at ");
        buffer.append(after);
        buffer.append("]</noscript>\n");

        return buffer.toString();
    }
}
