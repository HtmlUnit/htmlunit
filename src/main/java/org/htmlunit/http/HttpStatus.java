/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.http;

/**
 * Http Status Codes.
 *
 * @see <a href="https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml">
 *          IANA HTTP Status Code Registry</a>
 *
 * @author Ronald Brill
 */
public final class HttpStatus {

    // ---- 100 ----

    /** Continue / 100. */
    public static final int SC_CONTINUE_100 = 100;

    /** Switching Protocols / 101. */
    public static final int SC_SWITCHING_PROTOCOLS_101 = 101;

    /** Processing / 102. */
    public static final int SC_PROCESSING_102 = 102;

    // ---- 200 ----

    /** OK / 200. */
    public static final int SC_OK_200 = 200;
    /** OK / 200. */
    public static final String SC_OK_200_MSG = "OK";

    /** Created / 201. */
    public static final int SC_CREATED_201 = 201;

    /** Accepted / 202. */
    public static final int SC_ACCEPTED_202 = 202;

    /** Non Authoritative Information / 203. */
    public static final int SC_NON_AUTHORITATIVE_INFORMATION_203 = 203;

    /** No Content / 204. */
    public static final int SC_NO_CONTENT_204 = 204;
    /** No Content / 204. */
    public static final String SC_NO_CONTENT_204_MSG = "No Content";

    /** Reset Content / 205. */
    public static final int SC_RESET_CONTENT_205 = 205;

    /** Partial Content / 206. */
    public static final int SC_PARTIAL_CONTENT_206 = 206;

    /** Multi Status / 207. */
    public static final int SC_MULTI_STATUS_207 = 207;

    // ---- 300 ----

    /** Multiple Choices / 300. */
    public static final int SC_MULTIPLE_CHOICES_300 = 300;

    /** Moved Permanently / 301. */
    public static final int SC_MOVED_PERMANENTLY_301 = 301;

    /** Found / 302. */
    public static final int SC_FOUND_302 = 302;

    /** See Other / 303. */
    public static final int SC_SEE_OTHER_303 = 303;

    /** Not Modified / 304. */
    public static final int SC_NOT_MODIFIED_304 = 304;

    /** Use Proxy / 305. */
    public static final int SC_USE_PROXY_305 = 305;

    /** Temporary Redirect / 307. */
    public static final int SC_TEMPORARY_REDIRECT_307 = 307;

    /** Permanent Redirect / 308. */
    public static final int SC_PERMANENT_REDIRECT_308 = 308;

    // ---- 400 ----

    /** Bad Request / 400. */
    public static final int SC_BAD_REQUEST_400 = 400;

    /** Unauthorized / 401. */
    public static final int SC_UNAUTHORIZED_401 = 401;

    /** Payment Required / 402. */
    public static final int SC_PAYMENT_REQUIRED_402 = 402;

    /** Forbidden / 403. */
    public static final int SC_FORBIDDEN_403 = 403;

    /** Not Found / 404. */
    public static final int SC_NOT_FOUND_404 = 404;
    /** Not Found / 404. */
    public static final String SC_NOT_FOUND_404_MSG = "Not Found";

    /** Method Not Allowed / 405. */
    public static final int SC_METHOD_NOT_ALLOWED_405 = 405;

    /** Not Acceptable / 406. */
    public static final int SC_NOT_ACCEPTABLE_406 = 406;

    /** Proxy Authentication Required / 407. */
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED_407 = 407;

    /** Request Timeout / 408. */
    public static final int SC_REQUEST_TIMEOUT_408 = 408;

    /** Conflict / 409. */
    public static final int SC_CONFLICT_409 = 409;

    /** Gone / 410. */
    public static final int SC_GONE_410 = 410;

    /** Length Required / 411. */
    public static final int SC_LENGTH_REQUIRED_411 = 411;

    /** Precondition Failed / 412. */
    public static final int SC_PRECONDITION_FAILED_412 = 412;

    /** Payload Too Large / 413. */
    public static final int SC_PAYLOAD_TOO_LARGE_413 = 413;

    /** URI Too Long / 414. */
    public static final int SC_URI_TOO_LONG_414 = 414;

    /** Unsupported Media Type / 415. */
    public static final int SC_UNSUPPORTED_MEDIA_TYPE_415 = 415;

    /** Range Not Satisfiable / 416. */
    public static final int SC_RANGE_NOT_SATISFIABLE_416 = 416;

    /** Expectation Failed / 417. */
    public static final int SC_EXPECTATION_FAILED_417 = 417;

    /** Im A Teapot / 418. */
    public static final int SC_IM_A_TEAPOT_418 = 418;

    /** Enhance Your Calm / 420. */
    public static final int SC_ENHANCE_YOUR_CALM_420 = 420;

    /** Misdirected Request / 421. */
    public static final int SC_MISDIRECTED_REQUEST_421 = 421;

    /** Unprocessable Entity / 422. */
    public static final int SC_UNPROCESSABLE_ENTITY_422 = 422;

    /** Locked / 423. */
    public static final int SC_LOCKED_423 = 423;

    /** Failed Dependency / 424. */
    public static final int SC_FAILED_DEPENDENCY_424 = 424;

    /** Update Required / 426. */
    public static final int SC_UPGRADE_REQUIRED_426 = 426;

    /** Precondition Required / 428. */
    public static final int SC_PRECONDITION_REQUIRED_428 = 428;

    /** Too Many Requests / 429. */
    public static final int SC_TOO_MANY_REQUESTS_429 = 429;

    /** Request Header Fields Too Large / 431. */
    public static final int SC_REQUEST_HEADER_FIELDS_TOO_LARGE_431 = 431;

    /** Bad Unavailable For Legal Reasons / 451. */
    public static final int SC_UNAVAILABLE_FOR_LEGAL_REASONS_451 = 451;

    // ---- 500 ----

    /** Internal Server Error / 500. */
    public static final int SC_INTERNAL_SERVER_ERROR_500 = 500;

    /** Not Implemented / 501. */
    public static final int SC_NOT_IMPLEMENTED_501 = 501;

    /** Bad Gateway / 502. */
    public static final int SC_BAD_GATEWAY_502 = 502;

    /** Service Unavailable / 503. */
    public static final int SC_SERVICE_UNAVAILABLE_503 = 503;

    /** Gateway Timeout / 504. */
    public static final int SC_GATEWAY_TIMEOUT_504 = 504;

    /** Http Version Not Supported / 505. */
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED_505 = 505;

    /** Insufficient Storage / 507. */
    public static final int SC_INSUFFICIENT_STORAGE_507 = 507;

    /** Loop Detected / 508. */
    public static final int SC_LOOP_DETECTED_508 = 508;

    /** Not Extended / 510. */
    public static final int SC_NOT_EXTENDED_510 = 510;

    /** Network Authentication Required / 511. */
    public static final int SC_NETWORK_AUTHENTICATION_REQUIRED_511 = 511;

    private HttpStatus() {
    }
}
