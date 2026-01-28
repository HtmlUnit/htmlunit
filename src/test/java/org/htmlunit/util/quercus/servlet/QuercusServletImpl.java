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
 * @author Scott Ferguson
 */

package org.htmlunit.util.quercus.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.util.quercus.servlet.api.QuercusHttpServletRequestImpl;
import org.htmlunit.util.quercus.servlet.api.QuercusHttpServletResponseImpl;
import org.htmlunit.util.quercus.servlet.api.QuercusServletContextImpl;

import com.caucho.java.WorkDir;
import com.caucho.quercus.QuercusContext;
import com.caucho.quercus.QuercusDieException;
import com.caucho.quercus.QuercusErrorException;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.QuercusLineRuntimeException;
import com.caucho.quercus.QuercusRequestAdapter;
import com.caucho.quercus.QuercusRuntimeException;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.QuercusValueException;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.quercus.servlet.api.QuercusHttpServletRequest;
import com.caucho.util.CurrentTime;
import com.caucho.util.L10N;
import com.caucho.vfs.FilePath;
import com.caucho.vfs.Path;
import com.caucho.vfs.Vfs;
import com.caucho.vfs.WriteStream;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to call PHP through javax.script.
 */
@SuppressWarnings("serial")
public class QuercusServletImpl extends HttpServlet
{
  private static final L10N L = new L10N(QuercusServletImpl.class);
  // private static final Logger log = Logger.getLogger(QuercusServletImpl.class.getName());
  private static final Log log = LogFactory.getLog(QuercusServletImpl.class);

  protected QuercusContext _quercus;
  protected ServletConfig _config;
  protected ServletContext _servletContext;

  /**
   * initialize the script manager.
   */
  @Override
  public final void init(ServletConfig config)
    throws ServletException
  {
    _config = config;
    _servletContext = config.getServletContext();

    checkServletAPIVersion();

    Path pwd = new FilePath(_servletContext.getRealPath("/"));
    Path webInfDir = new FilePath(_servletContext.getRealPath("/WEB-INF"));

    getQuercus().setPwd(pwd);
    getQuercus().setWebInfDir(webInfDir);

    // need to set these for non-Resin containers
    if (! CurrentTime.isTest() && ! getQuercus().isResin()) {
      Vfs.setPwd(pwd);
      WorkDir.setLocalWorkDir(webInfDir.lookup("work"));
    }

    initImpl(config);

    getQuercus().init();
    getQuercus().start();
  }

  protected void initImpl(ServletConfig config)
    throws ServletException
  {
  }

  /**
   * Sets the profiling mode
   */
  public void setProfileProbability(double probability)
  {
  }

  /**
   * Makes sure the servlet container supports Servlet API 2.4+.
   */
  protected void checkServletAPIVersion()
  {
    int major = _servletContext.getMajorVersion();
    int minor = _servletContext.getMinorVersion();

    if (major < 2 || major == 2 && minor < 4)
      throw new QuercusRuntimeException(
          L.l("Quercus requires Servlet API 2.4+."));
  }

  /**
   * Service.
   */
  @Override
  public final void service(HttpServletRequest request,
                            HttpServletResponse response)
    throws ServletException, IOException
  {
    Env env = null;
    WriteStream ws = null;

    QuercusHttpServletRequestImpl req = new QuercusHttpServletRequestImpl(request);
    QuercusHttpServletResponseImpl res = new QuercusHttpServletResponseImpl(response);

    try {
      Path path = getPath(req);

      QuercusPage page;

      try {
        page = getQuercus().parse(path);
      }
      catch (FileNotFoundException e) {
        // php/2001
        // log.log(Level.FINER, e.toString(), e);
        log.debug(e.getMessage(), e);

        response.sendError(HttpServletResponse.SC_NOT_FOUND);

        return;
      }

      ws = openWrite(response);

      // php/2002
      // for non-Resin containers
      // for servlet filters that do post-request work after Quercus
      ws.setDisableCloseSource(true);

      // php/6006
      ws.setNewlineString("\n");

      QuercusContext quercus = getQuercus();

      env = quercus.createEnv(page, ws, req, res);

      // php/815d
      env.setPwd(path.getParent());

      quercus.setServletContext(new QuercusServletContextImpl(_servletContext));

      try {
        env.start();

        // php/2030, php/2032, php/2033
        // Jetty hides server classes from web-app
        // http://docs.codehaus.org/display/JETTY/Classloading
        //
        // env.setGlobalValue("request", env.wrapJava(request));
        // env.setGlobalValue("response", env.wrapJava(response));
        // env.setGlobalValue("servletContext", env.wrapJava(_servletContext));

        StringValue prepend
          = quercus.getIniValue("auto_prepend_file").toStringValue(env);
        if (prepend.length() > 0) {
          Path prependPath = env.lookup(prepend);

          if (prependPath == null)
            env.error(L.l("auto_prepend_file '{0}' not found.", prepend));
          else {
            QuercusPage prependPage = getQuercus().parse(prependPath);
            prependPage.executeTop(env);
          }
        }

        env.executeTop();

        StringValue append
          = quercus.getIniValue("auto_append_file").toStringValue(env);
        if (append.length() > 0) {
          Path appendPath = env.lookup(append);

          if (appendPath == null)
            env.error(L.l("auto_append_file '{0}' not found.", append));
          else {
            QuercusPage appendPage = getQuercus().parse(appendPath);
            appendPage.executeTop(env);
          }
        }
        //   return;
      }
      catch (QuercusExitException e) {
        throw e;
      }
      catch (QuercusErrorException e) {
        throw e;
      }
      catch (QuercusLineRuntimeException e) {
        // log.log(Level.FINE, e.toString(), e);
        log.debug(e.getMessage(), e);

        ws.println(e.getMessage());
        //  return;
      }
      catch (QuercusValueException e) {
        // log.log(Level.FINE, e.toString(), e);
        log.debug(e.getMessage(), e);

        ws.println(e.toString());

        //  return;
      }
      catch (StackOverflowError e) {
        RuntimeException myException
          = new RuntimeException(L.l("StackOverflowError at {0}", env.getLocation()), e);

        throw myException;
      }
      catch (Throwable e) {
        if (response.isCommitted())
          e.printStackTrace(ws.getPrintWriter());

        ws = null;

        throw e;
      }
      finally {
        // if (env != null)
          env.close();

        // don't want a flush for an exception
        if (ws != null && env.getDuplex() == null)
          ws.close();
      }
    }
    catch (QuercusDieException e) {
      // normal exit
      // log.log(Level.FINE, e.toString(), e);
      log.debug(e.getMessage(), e);
    }
    catch (QuercusExitException e) {
      // normal exit
      // log.log(Level.FINER, e.toString(), e);
      log.debug(e.getMessage(), e);
    }
    catch (QuercusErrorException e) {
      // error exit
      // log.log(Level.FINE, e.toString(), e);
      log.debug(e.getMessage(), e);
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (Throwable e) {
      handleThrowable(response, e);
    }
  }

  protected void handleThrowable(HttpServletResponse response, Throwable e)
    throws IOException, ServletException
  {
    throw new ServletException(e);
  }

  protected WriteStream openWrite(HttpServletResponse response)
    throws IOException
  {
    WriteStream ws;

    OutputStream out = response.getOutputStream();

    ws = Vfs.openWrite(out);

    return ws;
  }

  protected Path getPath(QuercusHttpServletRequest req)
  {
    // php/8173
    Path pwd = getQuercus().getPwd().copy();

    String servletPath = QuercusRequestAdapter.getPageServletPath(req);

    if (servletPath.startsWith("/")) {
      servletPath = servletPath.substring(1);
    }

    Path path = pwd.lookupChild(servletPath);

    // php/2010, php/2011, php/2012
    if (path.isFile()) {
      return path;
    }

    StringBuilder sb = new StringBuilder();

    sb.append(servletPath);

    String pathInfo = QuercusRequestAdapter.getPagePathInfo(req);

    if (pathInfo != null) {
      sb.append(pathInfo);
    }

    String scriptPath = sb.toString();

    path = pwd.lookupChild(scriptPath);

    return path;

    /* jetty getRealPath() de-references symlinks, which causes problems with MergePath
    // php/8173
    Path pwd = getQuercus().getPwd().copy();

    String scriptPath = QuercusRequestAdapter.getPageServletPath(req);
    String pathInfo = QuercusRequestAdapter.getPagePathInfo(req);

    Path path = pwd.lookup(req.getRealPath(scriptPath));

    if (path.isFile())
      return path;

    // XXX: include

    String fullPath;
    if (pathInfo != null)
      fullPath = scriptPath + pathInfo;
    else
      fullPath = scriptPath;

    return pwd.lookup(req.getRealPath(fullPath));
    */
  }

  /**
   * Returns the Quercus instance.
   */
  protected QuercusContext getQuercus()
  {
    if (_quercus == null) {
      _quercus = new QuercusContext();
    }

    return _quercus;
  }

  /**
   * Destroys the quercus instance.
   */
  @Override
  public void destroy()
  {
    _quercus.close();
  }
}

