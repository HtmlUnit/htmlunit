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
package com.gargoylesoftware.htmlunit.html;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Utility to handle conversion from HTML code to XML string.
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
class XmlSerializer {

    private static final String FILE_SEPARATOR = "/";
    private static final Pattern CREATE_FILE_PATTERN = Pattern.compile(".*/");

    private static final Log LOG = LogFactory.getLog(XmlSerializer.class);

    private final StringBuilder builder_ = new StringBuilder();
    private final StringBuilder indent_ = new StringBuilder();
    private File outputDir_;

    public void save(final SgmlPage page, final File file) throws IOException {
        String fileName = file.getName();
        if (!fileName.endsWith(".htm") && !fileName.endsWith(".html")) {
            fileName += ".html";
        }
        final File outputFile = new File(file.getParentFile(), fileName);
        if (outputFile.exists()) {
            throw new IOException("File already exists: " + outputFile);
        }
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        outputDir_ = new File(file.getParentFile(), fileName);

        // don't use asXml here because we have to sync the encoding from the
        // header with the one used by the writer
        final DomElement node = page.getDocumentElement();
        Charset charsetName = ISO_8859_1;
        builder_.setLength(0);
        indent_.setLength(0);
        if (page.isHtmlPage()) {
            charsetName = page.getCharset();
            if (charsetName != null && node instanceof HtmlHtml) {
                builder_.append("<?xml version=\"1.0\" encoding=\"").append(charsetName).append("\"?>").append('\n');
            }
        }
        printXml(node);
        final String response = builder_.toString();
        builder_.setLength(0);
        FileUtils.writeStringToFile(outputFile, response, charsetName);
    }

    /**
     * @param node a node
     * @return the xml representation according to the setting of this serializer
     * @throws IOException in case of problem saving resources
     */
    public String asXml(final DomElement node) throws IOException {
        builder_.setLength(0);
        indent_.setLength(0);
        final SgmlPage page = node.getPage();
        if (null != page && page.isHtmlPage()) {
            final Charset charsetName = page.getCharset();
            if (charsetName != null && node instanceof HtmlHtml) {
                builder_.append("<?xml version=\"1.0\" encoding=\"").append(charsetName).append("\"?>").append('\n');
            }
        }
        printXml(node);
        final String response = builder_.toString();
        builder_.setLength(0);
        return response;
    }

    protected void printXml(final DomElement node) throws IOException {
        if (!isExcluded(node)) {
            final boolean hasChildren = node.getFirstChild() != null;
            builder_.append(indent_).append('<');
            printOpeningTag(node);

            if (!hasChildren && !node.isEmptyXmlTagExpanded()) {
                builder_.append("/>").append('\n');
            }
            else {
                builder_.append('>').append('\n');
                for (DomNode child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                    indent_.append("  ");
                    if (child instanceof DomElement) {
                        printXml((DomElement) child);
                    }
                    else {
                        builder_.append(child);
                    }
                    indent_.setLength(indent_.length() - 2);
                }
                builder_.append(indent_).append("</").append(node.getTagName()).append('>').append('\n');
            }
        }
    }

    /**
     * @param node a node
     * @return the text representation according to the setting of this serializer
     * @throws IOException in case of problem saving resources
     */
    public String asText(final DomNode node) {
        builder_.setLength(0);

        if (node instanceof DomText) {
            builder_.append(((DomText) node).getData());
        }
        else {
            printText(node);
        }

        final String response = builder_.toString();
        builder_.setLength(0);
        return response;
    }

    protected void printText(final DomNode node) {
        for (DomNode child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof DomText) {
                builder_.append(((DomText) child).getData());
            }
            else {
                printText(child);
            }
        }
    }

    /**
     * Prints the content between "&lt;" and "&gt;" (or "/&gt;") in the output of the tag name
     * and its attributes in XML format.
     * @param node the node whose opening tag is to be printed
     * @throws IOException in case of problem saving resources
     */
    protected void printOpeningTag(final DomElement node) throws IOException {
        builder_.append(node.getTagName());
        final Map<String, DomAttr> attributes = readAttributes(node);

        for (final Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
            builder_.append(' ');
            builder_.append(entry.getKey());
            builder_.append("=\"");
            final String value = entry.getValue().getNodeValue();
            builder_.append(com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlAttributeValue(value));
            builder_.append('"');
        }
    }

    private Map<String, DomAttr> readAttributes(final DomElement node) throws IOException {
        if (node instanceof HtmlImage) {
            return getAttributesFor((HtmlImage) node);
        }
        else if (node instanceof HtmlLink) {
            return getAttributesFor((HtmlLink) node);
        }
        else if (node instanceof BaseFrameElement) {
            return getAttributesFor((BaseFrameElement) node);
        }

        Map<String, DomAttr> attributes = node.getAttributesMap();
        if (node instanceof HtmlOption) {
            attributes = new HashMap<>(attributes);
            final HtmlOption option = (HtmlOption) node;
            if (option.isSelected()) {
                if (!attributes.containsKey("selected")) {
                    attributes.put("selected", new DomAttr(node.getPage(), null, "selected", "selected", false));
                }
            }
            else {
                attributes.remove("selected");
            }
        }
        return attributes;
    }

    private Map<String, DomAttr> getAttributesFor(final BaseFrameElement frame) throws IOException {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(frame, DomElement.SRC_ATTRIBUTE);
        final DomAttr srcAttr = map.get(DomElement.SRC_ATTRIBUTE);
        if (srcAttr == null) {
            return map;
        }

        final Page enclosedPage = frame.getEnclosedPage();
        final String suffix = getFileExtension(enclosedPage);
        final File file = createFile(srcAttr.getValue(), "." + suffix);

        if (enclosedPage != null) {
            if (enclosedPage.isHtmlPage()) {
                file.delete(); // TODO: refactor as it is stupid to create empty file at one place
                // and then to complain that it already exists
                ((HtmlPage) enclosedPage).save(file);
            }
            else {
                try (InputStream is = enclosedPage.getWebResponse().getContentAsStream()) {
                    try (OutputStream fos = Files.newOutputStream(file.toPath())) {
                        IOUtils.copyLarge(is, fos);
                    }
                }
            }
        }

        srcAttr.setValue(file.getParentFile().getName() + FILE_SEPARATOR + file.getName());
        return map;
    }

    private static String getFileExtension(final Page enclosedPage) {
        if (enclosedPage != null) {
            if (enclosedPage.isHtmlPage()) {
                return "html";
            }

            final URL url = enclosedPage.getUrl();
            if (url.getPath().contains(".")) {
                return StringUtils.substringAfterLast(url.getPath(), ".");
            }
        }

        return ".unknown";
    }

    protected Map<String, DomAttr> getAttributesFor(final HtmlLink link) throws IOException {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(link, "href");
        final DomAttr hrefAttr = map.get("href");
        if (hrefAttr != null && StringUtils.isNotBlank(hrefAttr.getValue())) {
            final String protocol = link.getWebRequest().getUrl().getProtocol();
            if ("http".equals(protocol) || "https".equals(protocol)) {
                try {
                    final WebResponse response = link.getWebResponse(true, null);

                    final File file = createFile(hrefAttr.getValue(), ".css");
                    FileUtils.writeStringToFile(file, response.getContentAsString(), ISO_8859_1);
                    hrefAttr.setValue(outputDir_.getName() + FILE_SEPARATOR + file.getName());
                }
                catch (final IOException e) {
                    LOG.error("XmlSerializer: IOException while downloading link content from url '"
                                + hrefAttr + "'", e);
                }
                catch (final IllegalStateException e) {
                    LOG.error("XmlSerializer: IllegalStateException while downloading link content from url '"
                                + hrefAttr + "'", e);
                }
            }
        }

        return map;
    }

    protected Map<String, DomAttr> getAttributesFor(final HtmlImage image) {
        final Map<String, DomAttr> map = createAttributesCopyWithClonedAttribute(image, DomElement.SRC_ATTRIBUTE);
        final DomAttr srcAttr = map.get(DomElement.SRC_ATTRIBUTE);
        if (srcAttr != null && StringUtils.isNotBlank(srcAttr.getValue())) {
            try {
                final WebResponse response = image.getWebResponse(true);

                try (InputStream inputStream = response.getContentAsStream()) {
                    final File file = createFile(srcAttr.getValue(), "." + getSuffix(response));
                    FileUtils.copyInputStreamToFile(inputStream, file);

                    final String valueOnFileSystem = outputDir_.getName() + FILE_SEPARATOR + file.getName();
                    // this is the clone attribute node, not the original one of the page
                    srcAttr.setValue(valueOnFileSystem);
                }
            }
            catch (final IOException e) {
                LOG.error("XmlSerializer: IOException while downloading image content from url '" + srcAttr + "'", e);
            }
            catch (final IllegalStateException e) {
                LOG.error("XmlSerializer: IllegalStateException while downloading image content from url '"
                            + srcAttr + "'", e);
            }
        }

        return map;
    }

    private static String getSuffix(final WebResponse response) {
        // first try to take the one from the requested file
        final String url = response.getWebRequest().getUrl().toString();
        final String fileName = StringUtils.substringAfterLast(StringUtils.substringBefore(url, "?"), "/");
        // if there is a suffix with 2-4 letters, the take it
        final String suffix = StringUtils.substringAfterLast(fileName, ".");
        if (suffix.length() > 1 && suffix.length() < 5) {
            return suffix;
        }

        // use content type
        return MimeType.getFileExtension(response.getContentType());
    }

    private static Map<String, DomAttr> createAttributesCopyWithClonedAttribute(final HtmlElement elt,
            final String attrName) {
        final Map<String, DomAttr> newMap = new HashMap<>(elt.getAttributesMap());

        // clone the specified element, if possible
        final DomAttr attr = newMap.get(attrName);
        if (null == attr) {
            return newMap;
        }

        final DomAttr clonedAttr = new DomAttr(attr.getPage(), attr.getNamespaceURI(),
            attr.getQualifiedName(), attr.getValue(), attr.getSpecified());

        newMap.put(attrName, clonedAttr);

        return newMap;
    }

    protected boolean isExcluded(final DomElement element) {
        return element instanceof HtmlScript;
    }

    /**
     * Computes the best file to save the response to the given URL.
     * @param url the requested URL
     * @param extension the preferred extension
     * @return the file to create
     * @throws IOException if a problem occurs creating the file
     */
    private File createFile(final String url, final String extension) throws IOException {
        String name = url.replaceFirst("/$", "");
        name = CREATE_FILE_PATTERN.matcher(name).replaceAll("");
        name = StringUtils.substringBefore(name, "?"); // remove query
        name = StringUtils.substringBefore(name, ";"); // remove additional info
        name = StringUtils.substring(name, 0, 30); // many file systems have a limit at 255, let's limit it
        name = com.gargoylesoftware.htmlunit.util.StringUtils.sanitizeForFileName(name);
        if (!name.endsWith(extension)) {
            name += extension;
        }
        int counter = 0;
        while (true) {
            final String fileName;
            if (counter != 0) {
                fileName = StringUtils.substringBeforeLast(name, ".")
                    + "_" + counter + "." + StringUtils.substringAfterLast(name, ".");
            }
            else {
                fileName = name;
            }
            FileUtils.forceMkdir(outputDir_);
            final File f = new File(outputDir_, fileName);
            if (f.createNewFile()) {
                return f;
            }
            counter++;
        }
    }
}
