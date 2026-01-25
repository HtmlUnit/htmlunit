/*
 * Copyright (c) 1998-2018 Caucho Technology -- all rights reserved
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

import com.caucho.config.ConfigException;
import com.caucho.util.L10N;

/**
 * Custom class configuration.
 */
public class PhpClassConfig {
  private static final L10N L = new L10N(PhpClassConfig.class);

  private Class _type;
  private String _name;

  /**
   * Sets the class to add.
   */
  public void setType(Class cl)
  {
    _type = cl;
  }

  /**
   * Returns the name of the type to add.
   */
  public Class getType()
  {
    return _type;
  }

  /**
   * Sets the PHP name for the class
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Sets the PHP name for the class
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Initialize the class.
   */
  public void init()
    throws ConfigException
  {
    if (_type == null)
      throw new ConfigException(L.l("<class> requires a type."));

    if (_name == null) {
      String name = _type.getName();

      int p = name.lastIndexOf('.');

      _name = name.substring(p + 1);
    }
  }
}

