/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Resource not found (404 response).
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class NotFoundException extends ResponseException {

	private static final long serialVersionUID = 4758686274252544211L;

	/**
	 * Default constructor.
	 */
	public NotFoundException() {
		super(HttpStatus.NOT_FOUND, null, null);
	}

}
