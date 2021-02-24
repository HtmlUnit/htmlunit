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
package com.gargoylesoftware.htmlunit.general;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Ensures all defined Host classes in internet references (e.g. Mozilla Developer Network)
 * are defined in {@link HostClassNameTest}.
 *
 * @author Ahmed Ashour
 */
public final class HostExtractor {

    /**
     * Tests known prefixes.
     */
    public static final List<String> PREFIXES_ = Arrays.asList("HTML", "DOM", "SVG", "CSS", "JSON",
            "URL", "URI", "TCP", "RTC", "IDB", "MIDI", "VR", "SIMD");

    private HostExtractor() {
    }

    /**
     * The entry point.
     * @param args optional proxy hostname and port
     * @throws Exception if an error occurs
     */
    public static void main(final String[] args) throws Exception {
        final Set<String> set = new HashSet<>();
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            if (args.length > 1) {
                final ProxyConfig proxyConfig = new ProxyConfig(args[0], Integer.parseInt(args[1]));
                proxyConfig.addHostsToProxyBypass("localhost");
                webClient.getOptions().setProxyConfig(proxyConfig);
            }
            fillMDNWebAPI(webClient, set);
            fillMDNJavaScriptGlobalObjects(webClient, set);
            final String testRoot = "src/test/java/";
            ensure(new File(testRoot + HostClassNameTest.class.getName().replace('.', '/') + ".java"), set);
        }
    }

    private static void fillMDNWebAPI(final WebClient webClient, final Set<String> set) throws Exception {
        final HtmlPage page = webClient.getPage("https://developer.mozilla.org/en-US/docs/Web/API");
        for (final Object o : page.getByXPath("//*[@class='indexListTerm']")) {
            set.add(((HtmlElement) o).asText());
        }
    }

    private static void fillMDNJavaScriptGlobalObjects(final WebClient webClient, final Set<String> set)
        throws Exception {
        final HtmlPage page
            = webClient.getPage("https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects");
        for (final Object o : page.getByXPath("//*[name()='code']/text()")) {
            String value = o.toString();
            if (!value.isEmpty()) {
                if (value.endsWith("()")) {
                    value = value.substring(0, value.length() - 2);
                }

                set.add(value);
            }
        }
    }

    private static void ensure(final File file, final Set<String> set) throws IOException {
        final Set<String> unusedNames = new HashSet<>(set);
        final List<String> lines = FileUtils.readLines(file, ISO_8859_1);
        for (final String line : lines) {
            for (final Iterator<String> it = unusedNames.iterator(); it.hasNext();) {
                if (line.contains("(\"" + it.next() + "\")")) {
                    it.remove();
                }
            }
        }
        unusedNames.remove("this");
        unusedNames.remove("Boolean");
        unusedNames.remove("null");

        if (!unusedNames.isEmpty()) {
            for (final String name : unusedNames) {
                if (name.contains(" ")) {
                    continue;
                }
                System.out.println("");
                System.out.println("    /**");
                System.out.println("     * @throws Exception if the test fails");
                System.out.println("     */");
                System.out.println("    @Test");
                System.out.println("    @Alerts(\"exception\")");
                String methodName = name;
                for (final String prefix : PREFIXES_) {
                    if (methodName.startsWith(prefix)) {
                        methodName = prefix.toLowerCase(Locale.ROOT) + methodName.substring(prefix.length());
                        break;
                    }
                }
                if (Character.isUpperCase(methodName.charAt(0))) {
                    methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
                }
                methodName = methodName.replace(".", "_");
                System.out.println("    public void " + methodName + "() throws Exception {");
                System.out.println("        test(\"" + name + "\");");
                System.out.println("    }");
            }
        }
        for (final String name : unusedNames) {
            if (name.contains(" ")) {
                System.out.println("Ignoring: " + name);
            }
        }
    }
}
