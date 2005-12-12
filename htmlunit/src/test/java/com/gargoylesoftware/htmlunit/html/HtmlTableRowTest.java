/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import org.mozilla.javascript.ScriptableObject;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.HTMLElement;

/**
 * Tests for HtmlTableRow
 * 
 * @version $Revision$
 * @author <a href="mailto:gallaherm@pragmatics.com">Mike Gallaher </a>
 * @author Mike Bowler
 */
public class HtmlTableRowTest extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test
     */
    public HtmlTableRowTest(final String name) {
        super(name);
    }

    private static final String htmlContent = "<html><head><title>foo</title></head><body>"
            + "<table id='table'><tr id='row'>"
            + "<td id='cell' width='20'><input type='text' id='foo'/></td>"
            + "</tr></table>" + "</body></html>";

    private WebClient client_;

    private HtmlPage page_;
    private HtmlTable table_;
    private HtmlTableBody tbody_;
    private HtmlTableRow row_;
    private HtmlTableCell cell_;
    private HtmlTableRow rowClone_;
    private HtmlTableCell cellClone_;

    /**
     * {@inheritDoc} 
     */
    public void setUp() throws Exception {
        page_ = loadPage(htmlContent);
        client_ = page_.getWebClient();
        
        table_ = (HtmlTable) page_.getHtmlElementById("table");
        tbody_ = (HtmlTableBody) table_.getFirstChild();
        row_ = table_.getRow(0);
        cell_ = row_.getCell(0);

        rowClone_ = (HtmlTableRow) row_.cloneNode(true);
        cellClone_ = rowClone_.getCell(0);
    }

    /**
     * Ensure that cloneNode leaves the original node unchanged.
     */
    public void testClonePreservesOriginal() {
        assertSame(tbody_, row_.getParentNode());
        assertSame(row_, cell_.getParentNode());
        assertSame(cell_, row_.getCell(0));
        assertEquals("row", row_.getId());
        assertEquals("cell", cell_.getId());
    }

    /**
     * Ensure that the clones are not the originals.
     * @throws Exception if the test fails.
     */
    public void testClonesAreDistinct() throws Exception {
        assertNotSame(row_, rowClone_);
        assertNotSame(cell_, cellClone_);
    }

    /**
     * Ensure that the clones have the same page as the originals.
     */
    public void testCloneHasSamePage() {
        assertSame(cell_.getPage(), cellClone_.getPage());
        assertSame(row_.getPage(), rowClone_.getPage());
    }

    /**
     * Ensure that the cloned row has no parent.
     * @throws Exception if the test fails.
     */
    public void testClonedRowHasNullParent() throws Exception {
        assertNull(rowClone_.getParentNode());
    }

    /**
     * Ensure that the cloned row's children are not those of the original.
     * @throws Exception if the test fails.
     */
    public void testClonedRowHasDifferentChildren() throws Exception {
        assertEquals(row_.getCells().size(), rowClone_.getCells().size());
        assertNotSame(row_.getFirstChild(), rowClone_.getFirstChild());
    }

    /**
     * Ensure that the cloned cell's children are not those of the original.
     */
    public void testClonedCellHasDifferentChildren() {
        assertNotSame(cell_.getParentNode(), cellClone_.getParentNode());
        assertNotNull(cell_.getFirstChild());
        assertNotSame(cell_.getFirstChild(), cellClone_.getFirstChild());
    }

    /**
     * Ensure that the cloned cell has the cloned row as its parent.
     * @throws Exception if the test fails.
     */
    public void testClonedCellHasClonedRowAsParent() throws Exception {
        assertSame(rowClone_, cellClone_.getParentNode());
    }

    /**
     * Ensure the cloned row has a different id than the original.
     */
    public void testClonedRowHasDifferentId() {
        assertFalse(row_.getId().equals(rowClone_.getId()));
        assertEquals("", rowClone_.getId());
    }

    /**
     * Ensure the cloned cell has a different id than the original.
     */
    public void testClonedCellHasDifferentId() {
        assertFalse(cell_.getId().equals(cellClone_.getId()));
        assertEquals("", cellClone_.getId());
    }

    /**
     * Ensure the cloned cell's child has a different id than the original's
     * child.
     */
    public void testClonedCellChildHasDifferentId() {
        final HtmlElement cellChild = (HtmlElement) cell_.getFirstChild();
        final HtmlElement cellChildClone = (HtmlElement) cellClone_.getFirstChild();
        assertFalse(cellChild.getId().equals(cellChildClone.getId()));
        assertEquals("", cellChildClone.getId());
    }

    /**
     * Ensure the cloned cell's attribute value is the same as the original.
     */
    public void testCloneAttributesCopiedFromOriginal() {
        assertEquals("20", cell_.getAttributeValue("width"));
        assertEquals("20", cellClone_.getAttributeValue("width"));
    }

    /**
     * Ensure that changing the clone's attribute leaves the original's
     * attribute unchanged.
     */
    public void testCloneAttributeIsIndependentOfOriginal() {
        cellClone_.setAttributeValue("a", "one");
        assertFalse("one".equals(cell_.getAttributeValue("a")));
    }

    /**
     * Ensure that changing the original's attribute leaves the clone's
     * attribute unchanged.
     */
    public void testOriginalAttributeIsIndependentOfClone() {
        cell_.setAttributeValue("a", "one");
        assertFalse("one".equals(cellClone_.getAttributeValue("a")));
    }

    /**
     * Ensure that changing the clone's nodeValue leaves the original's
     * unchanged.
     */
    public void testCloneValueIsIndependentOfOriginal() {
        cellClone_.setNodeValue("one");
        assertFalse("one".equals(cell_.getNodeValue()));
    }

    /**
     * Ensure that changing the clone's id leaves the original's unchanged.
     */
    public void testCloneIdIsIndependentOfOriginal() {
        cellClone_.setNodeValue("one");
        assertFalse("one".equals(cell_.getNodeValue()));
    }

    // these next few test our assumptions about how scripts affect the DOM

    /**
     * Ensure that the JavaScript object returned by the script fragment really
     * refers to the same DOM node we think it should.
     */
    public void testScriptCanGetOriginalCell() {
        final String cmd = "document.getElementById('cell')";
        final Object object = client_.getScriptEngine().execute(page_, cmd, "test");

        final HtmlElement cellElement = ((HTMLElement) object).getHtmlElementOrDie();
        assertSame(cell_, cellElement);
    }

    /**
     * Ensure that the JavaScript object returned by the script fragment is the
     * same one the DOM node thinks it's wrapped by.
     */
    public void testCellScriptObjectIsReturnedByScript() {
        final String cmd = "document.getElementById('cell')";
        final HTMLElement jselement = (HTMLElement) client_.getScriptEngine()
                .execute(page_, cmd, "test");

        assertSame(jselement, cell_.getScriptObject());
    }

    /**
     * Ensure that setting a property via script sets the property on the
     * ScriptableObject that we think it should.
     */
    public void testScriptCanSetJsPropertyOnCell() {
        final String cmd = "document.getElementById('cell').a='original';document.getElementById('cell')";
        final Object object = client_.getScriptEngine().execute(page_, cmd, "test");

        final HTMLElement jselement = ((HTMLElement) object);
        assertEquals("original", ScriptableObject.getProperty(jselement, "a"));

        assertSame(jselement, cell_.getScriptObject());
    }

    /**
     * Ensure that a script can set the disabled property on a DOM node.
     */
    public void testCloneScriptCanSetDisabledOnCell() {
        final String cmd = "document.getElementById('cell').disabled='true'";
        client_.getScriptEngine().execute(page_, cmd, "test");
        assertEquals("disabled", cell_.getAttributeValue("disabled"));
    }

    /**
     * Ensure that a script can set an attribute on the DOM node.
     */
    public void testCloneScriptCanSetAttributeOnCell() {
        final String cmd = "document.getElementById('cell').setAttribute('a','original')";
        client_.getScriptEngine().execute(page_, cmd, "test");
        assertEquals("original", cell_.getAttributeValue("a"));
    }

    // these next few check that scripts manipulate the clone independently

    /**
     * Ensure that a script setting an attribute on the original does not affect
     * that same attribute on the clone.
     */
    public void testCloneScriptSetAttributeIndependentOfOriginal() {
        final String cmd = "document.getElementById('cell').setAttribute('a','original')";
        client_.getScriptEngine().execute(page_, cmd, "test");

        assertEquals("original", cell_.getAttributeValue("a"));
        assertFalse("original".equals(cellClone_.getAttributeValue("a")));
    }

    /**
     * Ensure that a script setting disabled on the original does not affect
     * that same attribute on the clone.
     */
    public void testCloneScriptSetDisabledIndependentOfOriginal() {
        final String cmd = "document.getElementById('cell').disabled = 'true'";
        client_.getScriptEngine().execute(page_, cmd, "test");

        assertEquals("disabled", cell_.getAttributeValue("disabled"));
        assertFalse("disabled".equals(cellClone_.getAttributeValue("disabled")));
    }

    /**
     * Ensure that a script referencing an element causes only that DOM element
     * to get a ScriptObject assigned, and does not cause one to be assigned to
     * the clone.
     */
    public void testCloneHasDifferentScriptableObject() {

        final String cmd = "document.getElementById('cell')"; // force it to have a
        // scriptable object
        client_.getScriptEngine().execute(page_, cmd, "test");

        assertNotSame(cell_.getScriptObject(), cellClone_.getScriptObject());
    }

    /**
     * Ensure that setting the value on a child of a table cell doesn't affect
     * the cloned child.
     */
    public void testScriptDomOperations() {
        final String cmd = "document.getElementById('foo').value = 'Input!';document.getElementById('foo')";
        client_.getScriptEngine().execute(page_, cmd, "test");

        final HtmlElement input = (HtmlElement) cell_.getFirstChild();
        assertEquals("Input!", input.getAttributeValue("value"));

        final HtmlElement inputClone = (HtmlElement) cellClone_.getFirstChild();
        assertFalse("Input!".equals(inputClone.getAttributeValue("value")));
    }
}
