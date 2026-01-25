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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import com.caucho.quercus.servlet.api.QuercusCookie;
import com.caucho.quercus.servlet.api.QuercusHttpServletRequest;
import com.caucho.quercus.servlet.api.QuercusHttpSession;
import com.caucho.quercus.servlet.api.QuercusRequestDispatcher;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class QuercusHttpServletRequestImpl implements QuercusHttpServletRequest
{
  private final HttpServletRequest _request;

  public QuercusHttpServletRequestImpl(HttpServletRequest request)
  {
    _request = request;
  }

  @Override
  public String getMethod()
  {
    return _request.getMethod();
  }

  @Override
  public String getHeader(String name)
  {
    return _request.getHeader(name);
  }

  @Override
  public Enumeration getHeaderNames()
  {
    return _request.getHeaderNames();
  }

  @Override
  public String getParameter(String name)
  {
    return _request.getParameter(name);
  }

  @Override
  public String []getParameterValues(String name)
  {
    return _request.getParameterValues(name);
  }

  @Override
  public Map<String,String[]> getParameterMap()
  {
    return _request.getParameterMap();
  }

  @Override
  public String getContentType()
  {
    return _request.getContentType();
  }

  @Override
  public String getCharacterEncoding()
  {
    return _request.getCharacterEncoding();
  }

  @Override
  public String getRequestURI()
  {
    return _request.getRequestURI();
  }

  @Override
  public String getQueryString()
  {
    return _request.getQueryString();
  }

  @Override
  public QuercusCookie []getCookies()
  {
    Cookie []cookies = _request.getCookies();

    if (cookies == null) {
      return new QuercusCookie[0];
    }

    QuercusCookie []qCookies = new QuercusCookie[cookies.length];

    for (int i = 0; i < cookies.length; i++) {
      qCookies[i] = new QuercusCookieImpl(cookies[i]);
    }

    return qCookies;
  }

  @Override
  public String getContextPath()
  {
    return _request.getContextPath();
  }

  @Override
  public String getServletPath()
  {
    return _request.getServletPath();
  }

  @Override
  public String getPathInfo()
  {
    return _request.getPathInfo();
  }

  @Override
  public String getRealPath(String path)
  {
    return _request.getRealPath(path);
  }

  @Override
  public InputStream getInputStream()
    throws IOException
  {
    return _request.getInputStream();
  }

  @Override
  public QuercusHttpSession getSession(boolean isCreate)
  {
    HttpSession session = _request.getSession(isCreate);

    if (session == null) {
      return null;
    }

    return new QuercusHttpSessionImpl(session);
  }

  @Override
  public String getLocalAddr()
  {
    return _request.getLocalAddr();
  }

  @Override
  public String getServerName()
  {
    return _request.getServerName();
  }

  @Override
  public int getServerPort()
  {
    return _request.getServerPort();
  }

  @Override
  public String getRemoteHost()
  {
    return _request.getRemoteHost();
  }

  @Override
  public String getRemoteAddr()
  {
    return _request.getRemoteAddr();
  }

  @Override
  public int getRemotePort()
  {
    return _request.getRemotePort();
  }

  @Override
  public String getRemoteUser()
  {
    return _request.getRemoteUser();
  }

  @Override
  public boolean isSecure()
  {
    return _request.isSecure();
  }

  @Override
  public String getProtocol()
  {
    return _request.getProtocol();
  }

  @Override
  public Object getAttribute(String name)
  {
    return _request.getAttribute(name);
  }

  @Override
  public String getIncludeRequestUri()
  {
    return (String) _request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI);
  }

  @Override
  public String getForwardRequestUri()
  {
    return (String) _request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
  }

  @Override
  public String getIncludeContextPath()
  {
    return (String) _request.getAttribute(RequestDispatcher.INCLUDE_CONTEXT_PATH);
  }

  @Override
  public String getIncludeServletPath()
  {
    return (String) _request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
  }

  @Override
  public String getIncludePathInfo()
  {
    return (String) _request.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
  }

  @Override
  public String getIncludeQueryString()
  {
    return (String) _request.getAttribute(RequestDispatcher.INCLUDE_QUERY_STRING);
  }

  @Override
  public QuercusRequestDispatcher getRequestDispatcher(String url)
  {
    RequestDispatcher dispatcher = _request.getRequestDispatcher(url);

    return new QuercusRequestDispatcherImpl(dispatcher);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T toRequest(Class<T> cls)
  {
    return (T) _request;
  }
}
