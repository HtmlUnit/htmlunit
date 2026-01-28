/* Copyright 2015 Google Inc. All Rights Reserved.

   Distributed under MIT license.
   See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
*/

package org.htmlunit.util.brotli;

/**
 * Unchecked exception used internally.
 */
class BrotliRuntimeException extends RuntimeException {

  BrotliRuntimeException(String message) {
    super(message);
  }

  BrotliRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
