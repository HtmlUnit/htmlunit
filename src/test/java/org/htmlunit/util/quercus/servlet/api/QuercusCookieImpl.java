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

import com.caucho.quercus.servlet.api.QuercusCookie;

import jakarta.servlet.http.Cookie;

public class QuercusCookieImpl implements QuercusCookie
{
  private final Cookie _cookie;

  public QuercusCookieImpl(Cookie cookie)
  {
    _cookie = cookie;
  }

  public QuercusCookieImpl(String name, String sessionId)
  {
    _cookie = new Cookie(name, sessionId);
  }

  @Override
  public String getName()
  {
    return _cookie.getName();
  }

  @Override
  public String getValue()
  {
    return _cookie.getValue();
  }

  @Override
  public void setVersion(int version)
  {
    _cookie.setVersion(version);
  }

  @Override
  public void setPath(String path)
  {
    _cookie.setPath(path);
  }

  @Override
  public void setMaxAge(int maxAge)
  {
    _cookie.setMaxAge(maxAge);
  }

  @Override
  public void setDomain(String domain)
  {
    _cookie.setDomain(domain);
  }

  @Override
  public void setSecure(boolean isSecure)
  {
    _cookie.setSecure(isSecure);
  }

  @Override
  public void setHttpOnly(boolean isHttpOnly)
  {
    _cookie.setHttpOnly(isHttpOnly);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T toCookie(Class<T> cls)
  {
    if (cls != Cookie.class) {
      throw new IllegalArgumentException("class must be a " + Cookie.class);
    }

    return (T) _cookie;
  }
}
