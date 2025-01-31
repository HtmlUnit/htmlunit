/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link DOMException}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class DOMExceptionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"})
    public void constants() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var properties = ['INDEX_SIZE_ERR', 'DOMSTRING_SIZE_ERR', 'HIERARCHY_REQUEST_ERR',"
            + " 'WRONG_DOCUMENT_ERR', 'INVALID_CHARACTER_ERR', 'NO_DATA_ALLOWED_ERR', 'NO_MODIFICATION_ALLOWED_ERR',"
            + " 'NOT_FOUND_ERR', 'NOT_SUPPORTED_ERR', 'INUSE_ATTRIBUTE_ERR', 'INVALID_STATE_ERR', 'SYNTAX_ERR',"
            + " 'INVALID_MODIFICATION_ERR', 'NAMESPACE_ERR', 'INVALID_ACCESS_ERR'];\n"
            + "  try {\n"
            + "    for (var i = 0; i < properties.length; i++) {\n"
            + "      log(DOMException[properties[i]]);\n"
            + "    }\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"IndexSizeError - 1 IndexSizeError",
             "INDEX_SIZE_ERR - 0 INDEX_SIZE_ERR",
             "HierarchyRequestError - 3 HierarchyRequestError",
             "HIERARCHY_REQUEST_ERR - 0 HIERARCHY_REQUEST_ERR",
             "WrongDocumentError - 4 WrongDocumentError",
             "WRONG_DOCUMENT_ERR - 0 WRONG_DOCUMENT_ERR",
             "InvalidCharacterError - 5 InvalidCharacterError",
             "INVALID_CHARACTER_ERR - 0 INVALID_CHARACTER_ERR",
             "NoModificationAllowedError - 7 NoModificationAllowedError",
             "NO_MODIFICATION_ALLOWED_ERR - 0 NO_MODIFICATION_ALLOWED_ERR",
             "NotFoundError - 8 NotFoundError",
             "NOT_FOUND_ERR - 0 NOT_FOUND_ERR",
             "NotSupportedError - 9 NotSupportedError",
             "NOT_SUPPORTED_ERR - 0 NOT_SUPPORTED_ERR",
             "InvalidStateError - 11 InvalidStateError",
             "INVALID_STATE_ERR - 0 INVALID_STATE_ERR",
             "InUseAttributeError - 10 InUseAttributeError",
             "INUSE_ATTRIBUTE_ERR - 0 INUSE_ATTRIBUTE_ERR",
             "SyntaxError - 12 SyntaxError",
             "SYNTAX_ERR - 0 SYNTAX_ERR",
             "InvalidModificationError - 13 InvalidModificationError",
             "INVALID_MODIFICATION_ERR - 0 INVALID_MODIFICATION_ERR",
             "NamespaceError - 14 NamespaceError",
             "NAMESPACE_ERR - 0 NAMESPACE_ERR",
             "InvalidAccessError - 15 InvalidAccessError",
             "INVALID_ACCESS_ERR - 0 INVALID_ACCESS_ERR",
             "TypeMismatchError - 17 TypeMismatchError",
             "TYPE_MISMATCH_ERR - 0 TYPE_MISMATCH_ERR",
             "SecurityError - 18 SecurityError",
             "SECURITY_ERR - 0 SECURITY_ERR",
             "NetworkError - 19 NetworkError",
             "NETWORK_ERR - 0 NETWORK_ERR",
             "AbortError - 20 AbortError",
             "ABORT_ERR - 0 ABORT_ERR",
             "URLMismatchError - 21 URLMismatchError",
             "URL_MISMATCH_ERR - 0 URL_MISMATCH_ERR",
             "QuotaExceededError - 22 QuotaExceededError",
             "QUOTA_EXCEEDED_ERR - 0 QUOTA_EXCEEDED_ERR",
             "TimeoutError - 23 TimeoutError",
             "TIMEOUT_ERR - 0 TIMEOUT_ERR",
             "InvalidNodeTypeError - 24 InvalidNodeTypeError",
             "INVALID_NODE_TYPE_ERR - 0 INVALID_NODE_TYPE_ERR",
             "DataCloneError - 25 DataCloneError",
             "DATA_CLONE_ERR - 0 DATA_CLONE_ERR",
             "EncodingError - 0 EncodingError",
             "NotReadableError - 0 NotReadableError",
             "UnknownError - 0 UnknownError",
             "ConstraintError - 0 ConstraintError",
             "DataError - 0 DataError",
             "TransactionInactiveError - 0 TransactionInactiveError",
             "ReadOnlyError - 0 ReadOnlyError",
             "VersionError - 0 VersionError",
             "OperationError - 0 OperationError",
             "NotAllowedError - 0 NotAllowedError"})
    public void name() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            // https://developer.mozilla.org/en-US/docs/Web/API/DOMException#error_names
            + "  var commonErrorNames = ["
                + "'IndexSizeError', 'INDEX_SIZE_ERR',"
                + "'HierarchyRequestError', 'HIERARCHY_REQUEST_ERR',"
                + "'WrongDocumentError', 'WRONG_DOCUMENT_ERR',"
                + "'InvalidCharacterError', 'INVALID_CHARACTER_ERR',"
                + "'NoModificationAllowedError', 'NO_MODIFICATION_ALLOWED_ERR',"
                + "'NotFoundError', 'NOT_FOUND_ERR',"
                + "'NotSupportedError', 'NOT_SUPPORTED_ERR',"
                + "'InvalidStateError', 'INVALID_STATE_ERR',"
                + "'InUseAttributeError', 'INUSE_ATTRIBUTE_ERR',"
                + "'SyntaxError', 'SYNTAX_ERR',"
                + "'InvalidModificationError', 'INVALID_MODIFICATION_ERR',"
                + "'NamespaceError', 'NAMESPACE_ERR',"
                + "'InvalidAccessError', 'INVALID_ACCESS_ERR',"
                + "'TypeMismatchError', 'TYPE_MISMATCH_ERR',"
                + "'SecurityError', 'SECURITY_ERR',"
                + "'NetworkError', 'NETWORK_ERR',"
                + "'AbortError', 'ABORT_ERR',"
                + "'URLMismatchError', 'URL_MISMATCH_ERR',"
                + "'QuotaExceededError', 'QUOTA_EXCEEDED_ERR',"
                + "'TimeoutError', 'TIMEOUT_ERR',"
                + "'InvalidNodeTypeError', 'INVALID_NODE_TYPE_ERR',"
                + "'DataCloneError', 'DATA_CLONE_ERR',"
                + "'EncodingError',"
                + "'NotReadableError',"
                + "'UnknownError',"
                + "'ConstraintError',"
                + "'DataError',"
                + "'TransactionInactiveError',"
                + "'ReadOnlyError',"
                + "'VersionError',"
                + "'OperationError',"
                + "'NotAllowedError'"
                + "];\n"
            + "  try {\n"
            + "    for (var i = 0; i < commonErrorNames.length; i++) {\n"
            + "      let ex = new DOMException('test', commonErrorNames[i]);"
            + "      log(commonErrorNames[i] + ' - ' + ex.code + ' ' + ex.name);\n"
            + "    }\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "urlMismatchERRoR"})
    public void nameCaseSensitive() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test', 'urlMismatchERRoR');"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "Error"})
    public void nameNotProvided() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test');"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "null"})
    public void nameNull() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test', null);"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "Error"})
    public void nameUndefined() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test', undefined);"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "unKnown"})
    public void nameUnknown() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test', 'unKnown');"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "7"})
    public void nameNumber() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    let ex = new DOMException('test', 7);"
            + "    log(ex.code);\n"
            + "    log(ex.name);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "undefined", "undefined"})
    public void properties() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(DOMException.code);\n"
            + "    log(DOMException.filename);\n"
            + "    log(DOMException.lineNumber);\n"
            + "    log(DOMException.message);\n"
            + "  } catch(e) { log(e.name);}\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test exception throw by an illegal DOM appendChild.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "true", "undefined", "undefined", "HIERARCHY_REQUEST_ERR: 3", "1"},
            FF = {"3", "true", "8", "§§URL§§", "HIERARCHY_REQUEST_ERR: 3", "1"},
            FF_ESR = {"3", "true", "8", "§§URL§§", "HIERARCHY_REQUEST_ERR: 3", "1"})
    /*
     * Messages:
     * CHROME: "A Node was inserted somewhere it doesn't belong."
     * FF: "Node cannot be inserted at the specified point in the hierarchy"
     */
    public void appendChild_illegal_node() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var htmlNode = document.documentElement;\n"
            + "  var body = document.body;\n"
            + "  try {\n"
            + "    body.appendChild(htmlNode);\n"
            + "  } catch(e) {\n"
            + "    log(e.code);\n"
            + "    log(e.message != null);\n"
            + "    log(e.lineNumber);\n"
            + "    log(e.filename);\n"
            + "    log('HIERARCHY_REQUEST_ERR: ' + e.HIERARCHY_REQUEST_ERR);\n"
            + "  }\n"
            + "  log(body.childNodes.length);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'><span>hi</span></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }
}
