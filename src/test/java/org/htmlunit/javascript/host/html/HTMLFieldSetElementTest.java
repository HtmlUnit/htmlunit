/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HTMLFieldSetElement}.
 *
 * @author George Murnock
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLFieldSetElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "undefined", "undefined", "undefined",
             "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void getAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <form>\n"
            + "    <fieldset id='f1' align='left' ></fieldset>\n"
            + "    <fieldset id='f2' align='right' ></fieldset>\n"
            + "    <fieldset id='f3' align='bottom' ></fieldset>\n"
            + "    <fieldset id='f4' align='middle' ></fieldset>\n"
            + "    <fieldset id='f5' align='top' ></fieldset>\n"
            + "    <fieldset id='f6' align='absbottom' ></fieldset>\n"
            + "    <fieldset id='f7' align='absmiddle' ></fieldset>\n"
            + "    <fieldset id='f8' align='baseline' ></fieldset>\n"
            + "    <fieldset id='f9' align='texttop' ></fieldset>\n"
            + "    <fieldset id='f10' align='wrong' ></fieldset>\n"
            + "    <fieldset id='f11' ></fieldset>\n"
            + "  </form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  for (var i = 1; i <= 11; i++) {\n"
            + "    log(document.getElementById('f' + i).align);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"CenTer", "8", "foo", "left", "right",
             "bottom", "middle", "top", "absbottom", "absmiddle", "baseline", "texttop"})
    public void setAlign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <form>\n"
            + "    <fieldset id='i1' align='left' />\n"
            + "  <form>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function setAlign(elem, value) {\n"
            + "    try {\n"
            + "      elem.align = value;\n"
            + "    } catch(e) { logEx(e); }\n"
            + "    log(elem.align);\n"
            + "  }\n"

            + "  var elem = document.getElementById('i1');\n"
            + "  setAlign(elem, 'CenTer');\n"

            + "  setAlign(elem, '8');\n"
            + "  setAlign(elem, 'foo');\n"

            + "  setAlign(elem, 'left');\n"
            + "  setAlign(elem, 'right');\n"
            + "  setAlign(elem, 'bottom');\n"
            + "  setAlign(elem, 'middle');\n"
            + "  setAlign(elem, 'top');\n"
            + "  setAlign(elem, 'absbottom');\n"
            + "  setAlign(elem, 'absmiddle');\n"
            + "  setAlign(elem, 'baseline');\n"
            + "  setAlign(elem, 'texttop');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLFormElement]")
    public void form() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <fieldset id='a' />\n"
            + "  </form>"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    log(document.getElementById('a').form);\n"
            + "  </script>"
            + "</body>"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void willValidate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "      log(document.getElementById('i5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='i1'>fs</fieldset>"
                + "    <fieldset id='i2' disabled></fieldset>"
                + "    <fieldset id='i3' hidden></fieldset>"
                + "    <fieldset id='i4' readonly></fieldset>"
                + "    <fieldset id='i5' style='display: none'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "true", "true", "true"})
    public void willValidateChild() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i2').willValidate);\n"
                + "      log(document.getElementById('i3').willValidate);\n"
                + "      log(document.getElementById('i4').willValidate);\n"
                + "      log(document.getElementById('i5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset><input id='i1'></fieldset>"
                + "    <fieldset disabled><input id='i2'></fieldset>"
                + "    <fieldset hidden><input id='i3'></fieldset>"
                + "    <fieldset readonly><input id='i4'></fieldset>"
                + "    <fieldset style='display: none'><input id='i5'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void checkValidityAlwaysTrueOnFieldsetItself() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').checkValidity());\n"
                + "      log(document.getElementById('i2').checkValidity());\n"
                + "      log(document.getElementById('i3').checkValidity());\n"
                + "      log(document.getElementById('i4').checkValidity());\n"
                + "      log(document.getElementById('i5').checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='i1'>fs</fieldset>"
                + "    <fieldset id='i2' disabled></fieldset>"
                + "    <fieldset id='i3' hidden></fieldset>"
                + "    <fieldset id='i4' readonly></fieldset>"
                + "    <fieldset id='i5' style='display: none'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true", "true"})
    public void reportValidityAlwaysTrueOnFieldsetItself() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').reportValidity());\n"
                + "      log(document.getElementById('i2').reportValidity());\n"
                + "      log(document.getElementById('i3').reportValidity());\n"
                + "      log(document.getElementById('i4').reportValidity());\n"
                + "      log(document.getElementById('i5').reportValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='i1'>fs</fieldset>"
                + "    <fieldset id='i2' disabled></fieldset>"
                + "    <fieldset id='i3' hidden></fieldset>"
                + "    <fieldset id='i4' readonly></fieldset>"
                + "    <fieldset id='i5' style='display: none'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Since a fieldset is never itself a candidate for constraint
     * validation, a custom validity message set on it must have NO effect on
     * checkValidity() -- must still report true, and .validity.valid must
     * still be true.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "true", ""})
    public void setCustomValidityOnFieldset_doesNotAffectCheckValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      f.setCustomValidity('some error');\n"
                + "      log(f.willValidate);\n"
                + "      log(f.validity.valid);\n"
                + "      log(f.checkValidity());\n"
                + "      log(f.validationMessage);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Flag disabled must propagate through a non-fieldset wrapper element (e.g. a
     * plain &lt;div&gt;), not just to a DIRECT child of the fieldset.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false"})
    public void disabledPropagatesThroughWrapperElement() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i1').disabled);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset disabled><div><span><input id='i1'></span></div></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Flag disabled on an OUTER fieldset must propagate transitively through a
     * nested INNER fieldset (which is not itself marked disabled) down to the
     * inner fieldset's own descendants.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "false"})
    public void disabledPropagatesThroughNestedFieldsets() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('inner').willValidate);\n"
                + "      log(document.getElementById('i1').willValidate);\n"
                + "      log(document.getElementById('i1').disabled);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset disabled>"
                + "      <fieldset id='inner'><input id='i1'></fieldset>"
                + "    </fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A form control inside the FIRST &lt;legend&gt; of a disabled
     * fieldset must NOT be disabled by it, while a control elsewhere in the
     * same fieldset's body MUST be disabled.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false", "false", "false"})
    public void disabledDoesNotApplyToControlsInsideFirstLegend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('inLegend').willValidate);\n"
                + "      log(document.getElementById('inLegend').disabled);\n"
                + "      log(document.getElementById('inBody').willValidate);\n"
                + "      log(document.getElementById('inBody').disabled);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset disabled>"
                + "      <legend><input id='inLegend'></legend>"
                + "      <input id='inBody'>"
                + "    </fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The legend exception applies ONLY to the FIRST legend child --
     * a control inside a SECOND legend element (technically non-conforming
     * markup, but browsers still parse it) must still be disabled.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void disabledExceptionAppliesOnlyToFirstLegend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('inFirstLegend').willValidate);\n"
                + "      log(document.getElementById('inSecondLegend').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset disabled>"
                + "      <legend><input id='inFirstLegend'></legend>"
                + "      <legend><input id='inSecondLegend'></legend>"
                + "    </fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The legend exception must NOT apply if the fieldset itself isn't
     * disabled in the first place -- baseline sanity check that a control
     * inside a legend of an ENABLED fieldset just behaves normally.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void legendControlUnaffectedWhenFieldsetNotDisabled() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('inLegend').willValidate);\n"
                + "      log(document.getElementById('inLegend').disabled);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset>"
                + "      <legend><input id='inLegend'></legend>"
                + "    </fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Toggling fieldset.disabled via script at runtime must propagate to a
     * child control's willValidate/disabled state immediately, both when
     * turning it on and back off.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "false", "false", "false", "true"})
    public void dynamicDisabledTogglePropagatesToChildren() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      var i = document.getElementById('i1');\n"
                + "      log(i.disabled);\n"
                + "      log(i.willValidate);\n"

                + "      f.disabled = true;\n"
                + "      log(i.disabled);\n"
                + "      log(i.willValidate);\n"

                + "      f.disabled = false;\n"
                + "      log(i.disabled);\n"
                + "      log(i.willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'><input id='i1'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * HTMLFieldSetElement.elements should return an HTMLCollection of the
     * fieldset's form-associated descendants, mirroring HTMLFormElement's
     * .elements.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "i1", "i2"})
    public void elementsContainsFormAssociatedDescendants() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      log(f.elements[0].id);\n"
                + "      log(f.elements[1].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1'>\n"
                + "      <textarea id='i2'></textarea>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * HTMLFieldSetElement.type must always report the constant "fieldset",
     * regardless of any attributes on the element.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("fieldset")
    public void typeIsAlwaysFieldset() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('f').type);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f' disabled></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The 'name' attribute should be reflected as the .name property.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"myFieldset", ""})
    public void nameAttributeReflectedAsProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('f1').name);\n"
                + "      log(document.getElementById('f2').name);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f1' name='myFieldset'></fieldset>"
                + "    <fieldset id='f2'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Direct-child listed elements (input, textarea, select, button) must all
     * appear in .elements, in document order.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"4", "i1", "i2", "i3", "i4"})
    public void elementsBasicInclusionAndOrder() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      for (var i = 0; i < f.elements.length; i++) {\n"
                + "        log(f.elements[i].id);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1'>\n"
                + "      <select id='i2'></select>\n"
                + "      <textarea id='i3'></textarea>\n"
                + "      <button id='i4'></button>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A listed element must still be included even when nested inside a plain
     * (non-fieldset) wrapper element, not just as a direct child.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "i1"})
    public void elementsIncludeNestedThroughWrapper() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      log(f.elements[0].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <div><span><input id='i1'></span></div>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Non-listed elements (plain containers, and the fieldset's own &lt;legend&gt;)
     * must NOT appear in .elements, even though legend is a direct, meaningful
     * child of the fieldset.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "onlyListed"})
    public void elementsExcludeNonListedElementsAndLegend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      log(f.elements[0].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <legend id='leg'>caption</legend>\n"
                + "      <div id='wrapper'></div>\n"
                + "      <span id='span1'></span>\n"
                + "      <p id='p1'>text</p>\n"
                + "      <input id='onlyListed'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An input with type='image' must be included from .elements.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "imageInput", "textInput"})
    public void elementsIcludeImageTypeInput() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      log(f.elements[0].id);\n"
                + "      log(f.elements[1].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='imageInput' type='image'>\n"
                + "      <input id='textInput' type='text'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An outer fieldset's {@code elements} collection includes listed elements
     * contained within nested fieldsets, as well as the nested fieldset elements
     * themselves.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "outerInput", "inner", "innerInput"})
    public void elementsExcludeDescendantsOfNestedFieldset() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var outer = document.getElementById('outer');\n"
                + "      log(outer.elements.length);\n"
                + "      for (var i = 0; i < outer.elements.length; i++) {\n"
                + "        log(outer.elements[i].id);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='outer'>\n"
                + "      <input id='outerInput'>\n"
                + "      <fieldset id='inner'>\n"
                + "        <input id='innerInput'>\n"
                + "      </fieldset>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The INNER fieldset's own .elements must
     * include its own descendant, since the inner fieldset IS that element's
     * closest fieldset ancestor.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "innerInput"})
    public void elementsOfInnerFieldsetIncludeItsOwnDescendants() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var inner = document.getElementById('inner');\n"
                + "      log(inner.elements.length);\n"
                + "      log(inner.elements[0].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='outer'>\n"
                + "      <input id='outerInput'>\n"
                + "      <fieldset id='inner'>\n"
                + "        <input id='innerInput'>\n"
                + "      </fieldset>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The inner &lt;fieldset&gt; element ITSELF (as opposed to its
     * descendants) is a listed element whose closest fieldset ancestor is the
     * OUTER fieldset -- so the inner fieldset element must appear in the
     * outer's .elements, even though the inner fieldset's own children do not.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void elementsOfOuterFieldsetIncludeInnerFieldsetItself() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var outer = document.getElementById('outer');\n"
                + "      var found = false;\n"
                + "      for (var i = 0; i < outer.elements.length; i++) {\n"
                + "        if (outer.elements[i].id === 'inner') { found = true; }\n"
                + "      }\n"
                + "      log(found);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='outer'>\n"
                + "      <fieldset id='inner'>\n"
                + "        <input id='innerInput'>\n"
                + "      </fieldset>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Triple nesting sanity check: with three levels of fieldset nesting, the
     * OUTERMOST fieldset's .elements must exclude descendants of BOTH inner
     * levels, not just the immediately-nested one.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"3", "middle", "innermost", "deepInput"})
    public void elementsExcludeDescendantsAcrossTripleNesting() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var outer = document.getElementById('outer');\n"
                + "      log(outer.elements.length);\n"
                + "      for (var i = 0; i < outer.elements.length; i++) {\n"
                + "        log(outer.elements[i].id);\n"
                + "      }\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='outer'>\n"
                + "      <fieldset id='middle'>\n"
                + "        <fieldset id='innermost'>\n"
                + "          <input id='deepInput'>\n"
                + "        </fieldset>\n"
                + "      </fieldset>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A control physically nested inside the fieldset, but explicitly
     * associated with a DIFFERENT form via the 'form' attribute, must still
     * appear in the fieldset's .elements -- inclusion is tree-based, not
     * form-ownership-based.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "i1"})
    public void elementsIncludeControlRegardlessOfFormAttribute() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "      log(f.elements[0].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form id='form1'>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1' form='otherForm'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "  <form id='otherForm'></form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A control that IS associated with the same form via the 'form' attribute,
     * but sits OUTSIDE the fieldset entirely in the tree, must NOT appear in
     * that fieldset's .elements.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void elementsExcludeControlOutsideFieldsetEvenIfSameForm() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.length);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form id='form1'>\n"
                + "    <fieldset id='f'></fieldset>\n"
                + "    <input id='outsideInput'>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The .elements must be a LIVE collection: appending a new listed element after
     * the initial read must be reflected without re-querying.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "2", "added"})
    public void elementsIsLiveOnAppend() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      var live = f.elements;\n"
                + "      log(live.length);\n"

                + "      var newInput = document.createElement('input');\n"
                + "      newInput.id = 'added';\n"
                + "      f.appendChild(newInput);\n"
                + "      log(live.length);\n"
                + "      log(live[live.length - 1].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The .elements must also live-update on removal.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"2", "1"})
    public void elementsIsLiveOnRemove() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      var live = f.elements;\n"
                + "      log(live.length);\n"

                + "      f.removeChild(document.getElementById('i1'));\n"
                + "      log(live.length);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1'>\n"
                + "      <input id='i2'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Named access: an element with a matching 'name' attribute should be
     * reachable via elements.namedItem(...) and bracket/dot-style named access,
     * mirroring HTMLFormElement.elements' named-getter behavior.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"i1", "i1"})
    public void elementsNamedAccess() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      log(f.elements.namedItem('myName').id);\n"
                + "      log(f.elements['myName'].id);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'>\n"
                + "      <input id='i1' name='myName'>\n"
                + "    </fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An empty fieldset (no listed descendants at all) reports
     * .elements.length as 0, not an error/undefined.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void elementsEmptyFieldset() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('f').elements.length);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'></fieldset>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * A fieldset with a custom validity message set must
     * never fire 'invalid' via checkValidity().
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void fieldsetCheckValidityNeverFiresInvalidEvenWithCustomValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      f.addEventListener('invalid', function() { log('unexpected invalid fired'); });\n"
                + "      f.setCustomValidity('some error');\n"
                + "      log(f.checkValidity());\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reportValidity() must also never focus the fieldset, since it
     * never counts as invalid in the first place.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    public void fieldsetReportValidityNeverFocusesEvenWithCustomValidity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      var f = document.getElementById('f');\n"
                + "      f.setCustomValidity('some error');\n"
                + "      log(f.reportValidity());\n"
                + "      log(document.activeElement === f);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <fieldset id='f'></fieldset>"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
