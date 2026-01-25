/*
 * Copyright (c) 1998-2014 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Nam Nguyen
 */

package org.htmlunit.util.quercus.servlet.api;

import java.io.IOException;
import java.io.OutputStream;

import com.caucho.quercus.servlet.api.QuercusCookie;
import com.caucho.quercus.servlet.api.QuercusHttpServletResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class QuercusHttpServletResponseImpl implements QuercusHttpServletResponse
{
  private final HttpServletResponse _response;

  public QuercusHttpServletResponseImpl(HttpServletResponse response)
  {
    _response = response;
  }

  @Override
  public void setContentType(String type)
  {
    _response.setContentType(type);
  }

  @Override
  public void addHeader(String name, String value)
  {
    _response.addHeader(name, value);
  }

  @Override
  public void setHeader(String name, String value)
  {
    _response.setHeader(name, value);
  }

  @Override
  public void setStatus(int code, String status)
  {
    _response.setStatus(code, status);
  }

  @Override
  public String getCharacterEncoding()
  {
    return _response.getCharacterEncoding();
  }

  @Override
  public void setCharacterEncoding(String encoding)
  {
    _response.setCharacterEncoding(encoding);
  }

  @Override
  public void addCookie(QuercusCookie cookie)
  {
    _response.addCookie(cookie.toCookie(Cookie.class));
  }

  @Override
  public boolean isCommitted()
  {
    return _response.isCommitted();
  }

  @Override
  public OutputStream getOutputStream()
    throws IOException
  {
    return _response.getOutputStream();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T toResponse(Class<T> cls)
  {
    return (T) _response;
  }
}
