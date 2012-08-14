/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * Tests for {@link HtmlTableRow}.
 *
 * @version $Revision$
 * @author <a href="mailto:gallaherm@pragmatics.com">Mike Gallaher</a>
 * @author Mike Bowler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlTableRowTest extends WebTestCase {

    private static final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<table id='table'><tr id='row'>\n"
            + "<td id='cell' width='20'><input type='text' id='foo'/></td>\n"
            + "</tr></table>\n" + "</body></html>";

    private HtmlPage page_;
    private HtmlTable table_;
    private HtmlTableBody tbody_;
    private HtmlTableRow row_;
    private HtmlTableCell cell_;
    private HtmlTableRow rowClone_;
    private HtmlTableCell cellClone_;

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    @Before
    public void init() throws Exception {
        page_ = loadPage(htmlContent);

        table_ = page_.getHtmlElementById("table");
        tbody_ = (HtmlTableBody) table_.getFirstChild();
        row_ = table_.getRow(0);
        cell_ = row_.getCell(0);

        rowClone_ = (HtmlTableRow) row_.cloneNode(true);
        cellClone_ = rowClone_.getCell(0);
    }

    /**
     * Ensure that cloneNode leaves the original node unchanged.
     */
    @Test
    public void testClonePreservesOriginal() {
        assertSame(tbody_, row_.getParentNode());
        assertSame(row_, cell_.getParentNode());
        assertSame(cell_, row_.getCell(0));
        assertEquals("row", row_.getId());
        assertEquals("cell", cell_.getId());
    }

    /**
     * Ensure that the clones are not the originals.
     * @throws Exception if the test fails
     */
    @Test
    public void testClonesAreDistinct() throws Exception {
        assertNotSame(row_, rowClone_);
        assertNotSame(cell_, cellClone_);
    }

    /**
     * Ensure that the clones have the same page as the originals.
     */
    @Test
    public void testCloneHasSamePage() {
        assertSame(cell_.getPage(), cellClone_.getPage());
        assertSame(row_.getPage(), rowClone_.getPage());
    }

    /**
     * Ensure that the cloned row has no parent.
     * @throws Exception if the test fails
     */
    @Test
    public void testClonedRowHasNullParent() throws Exception {
        assertNull(rowClone_.getParentNode());
    }

    /**
     * Ensure that the cloned row's children are not those of the original.
     * @throws Exception if the test fails
     */
    @Test
    public void testClonedRowHasDifferentChildren() throws Exception {
        assertEquals(row_.getCells().size(), rowClone_.getCells().size());
        assertNotSame(row_.getFirstChild(), rowClone_.getFirstChild());
    }

    /**
     * Ensure that the cloned cell's children are not those of the original.
     */
    @Test
    public void testClonedCellHasDifferentChildren() {
        assertNotSame(cell_.getParentNode(), cellClone_.getParentNode());
        assertNotNull(cell_.getFirstChild());
        assertNotSame(cell_.getFirstChild(), cellClone_.getFirstChild());
    }

    /**
     * Ensure that the cloned cell has the cloned row as its parent.
     * @throws Exception if the test fails
     */
    @Test
    public void testClonedCellHasClonedRowAsParent() throws Exception {
        assertSame(rowClone_, cellClone_.getParentNode());
    }

    /**
     * Ensure the cloned cell's attribute value is the same as the original.
     */
    @Test
    public void testCloneAttributesCopiedFromOriginal() {
        assertEquals("20", cell_.getAttribute("width"));
        assertEquals("20", cellClone_.getAttribute("width"));
    }

    /**
     * Ensure that changing the clone's attribute leaves the original's
     * attribute unchanged.
     */
    @Test
    public void testCloneAttributeIsIndependentOfOriginal() {
        cellClone_.setAttribute("a", "one");
        assertFalse("one".equals(cell_.getAttribute("a")));
    }

    /**
     * Ensure that changing the original's attribute leaves the clone's
     * attribute unchanged.
     */
    @Test
    public void testOriginalAttributeIsIndependentOfClone() {
        cell_.setAttribute("a", "one");
        assertFalse("one".equals(cellClone_.getAttribute("a")));
    }

    /**
     * Ensure that changing the clone's nodeValue leaves the original's
     * unchanged.
     */
    @Test
    public void testCloneValueIsIndependentOfOriginal() {
        cellClone_.setNodeValue("one");
        assertFalse("one".equals(cell_.getNodeValue()));
    }

    /**
     * Ensure that changing the clone's id leaves the original's unchanged.
     */
    @Test
    public void testCloneIdIsIndependentOfOriginal() {
        cellClone_.setNodeValue("one");
        assertFalse("one".equals(cell_.getNodeValue()));
    }

    // these next few test our assumptions about how scripts affect the DOM

    /**
     * Ensure that the JavaScript object returned by the script fragment really
     * refers to the same DOM node we think it should.
     */
    @Test
    public void testScriptCanGetOriginalCell() {
        final String cmd = "document.getElementById('cell')";
        final Object object = page_.executeJavaScript(cmd).getJavaScriptResult();

        final HtmlElement cellElement = ((HTMLElement) object).getDomNodeOrDie();
        assertSame(cell_, cellElement);
    }

    /**
     * Ensure that the JavaScript object returned by the script fragment is the
     * same one the DOM node thinks it's wrapped by.
     */
    @Test
    public void testCellScriptObjectIsReturnedByScript() {
        final String cmd = "document.getElementById('cell')";
        final HTMLElement jselement = (HTMLElement) page_.executeJavaScript(cmd).getJavaScriptResult();

        assertSame(jselement, cell_.getScriptObject());
    }

    /**
     * Ensure that setting a property via script sets the property on the
     * ScriptableObject that we think it should.
     */
    @Test
    public void testScriptCanSetJsPropertyOnCell() {
        final String cmd = "document.getElementById('cell').a='original';document.getElementById('cell')";
        final Object object = page_.executeJavaScript(cmd).getJavaScriptResult();

        final HTMLElement jselement = ((HTMLElement) object);
        assertEquals("original", ScriptableObject.getProperty(jselement, "a"));

        assertSame(jselement, cell_.getScriptObject());
    }

    /**
     * Ensure that a script can set the disabled property on a DOM node.
     */
    @Test
    @NotYetImplemented(Browser.FF)
    public void testCloneScriptCanSetDisabledOnCell() {
        final String cmd = "document.getElementById('cell').disabled='true'";
        page_.executeJavaScript(cmd);
        assertEquals("disabled", cell_.getAttribute("disabled"));
    }

    /**
     * Ensure that a script can set an attribute on the DOM node.
     */
    @Test
    public void testCloneScriptCanSetAttributeOnCell() {
        final String cmd = "document.getElementById('cell').setAttribute('a','original')";
        page_.executeJavaScript(cmd);
        assertEquals("original", cell_.getAttribute("a"));
    }

    // these next few check that scripts manipulate the clone independently

    /**
     * Ensure that a script setting an attribute on the original does not affect
     * that same attribute on the clone.
     */
    @Test
    public void testCloneScriptSetAttributeIndependentOfOriginal() {
        final String cmd = "document.getElementById('cell').setAttribute('a','original')";
        page_.executeJavaScript(cmd);

        assertEquals("original", cell_.getAttribute("a"));
        assertFalse("original".equals(cellClone_.getAttribute("a")));
    }

    /**
     * Ensure that a script setting disabled on the original does not affect
     * that same attribute on the clone.
     */
    @Test
    @NotYetImplemented(Browser.FF)
    public void testCloneScriptSetDisabledIndependentOfOriginal() {
        final String cmd = "document.getElementById('cell').disabled = 'true'";
        page_.executeJavaScript(cmd);

        assertEquals("disabled", cell_.getAttribute("disabled"));
        assertFalse("disabled".equals(cellClone_.getAttribute("disabled")));
    }

    /**
     * Ensure that a script referencing an element causes only that DOM element
     * to get a ScriptObject assigned, and does not cause one to be assigned to
     * the clone.
     */
    @Test
    public void testCloneHasDifferentScriptableObject() {
        final String cmd = "document.getElementById('cell')"; // force it to have a
        // scriptable object
        page_.executeJavaScript(cmd);

        assertNotSame(cell_.getScriptObject(), cellClone_.getScriptObject());
    }

    /**
     * Ensure that setting the value on a child of a table cell doesn't affect
     * the cloned child.
     */
    @Test
    public void testScriptDomOperations() {
        final String cmd = "document.getElementById('foo').value = 'Input!';document.getElementById('foo')";
        page_.executeJavaScript(cmd);

        final HtmlElement input = (HtmlElement) cell_.getFirstChild();
        assertEquals("Input!", input.getAttribute("value"));

        final HtmlElement inputClone = (HtmlElement) cellClone_.getFirstChild();
        assertFalse("Input!".equals(inputClone.getAttribute("value")));
    }
}
