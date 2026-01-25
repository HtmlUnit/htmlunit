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

import com.caucho.quercus.servlet.api.QuercusHttpServletRequest;
import com.caucho.quercus.servlet.api.QuercusHttpServletResponse;
import com.caucho.quercus.servlet.api.QuercusRequestDispatcher;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class QuercusRequestDispatcherImpl implements QuercusRequestDispatcher
{
  private final RequestDispatcher _dispatcher;

  public QuercusRequestDispatcherImpl(RequestDispatcher dispatcher)
  {
    _dispatcher = dispatcher;
  }

  @Override
  public void include(QuercusHttpServletRequest request,
                      QuercusHttpServletResponse response)
    throws Exception
  {
    HttpServletRequest req = request.toRequest(HttpServletRequest.class);
    HttpServletResponse res = response.toResponse(HttpServletResponse.class);

    _dispatcher.include(req, res);
  }
}
