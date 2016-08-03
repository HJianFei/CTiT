/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution. 
 *
 * The Eclipse Public License is available at 
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */
package com.hjf.ctit.sqlutils;

public class PersistenceException extends Exception {

  /**
   * Creates a persistence exception with the given error message
   * @param message The error message to display
   */
  public PersistenceException(String message) {
    super(message);
  }

  /** Serialisation ID**/
  private static final long serialVersionUID = 5326458803268855071L;

}
