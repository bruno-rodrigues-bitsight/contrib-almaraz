/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception when the access to a resource is forbidden (403 status code).
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ForbiddenException extends ResponseException {


	private static final long serialVersionUID = -8256235138025666145L;

	/**
	 * Default constructor.
	 */
	public ForbiddenException() {
		this(null);
	}

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public ForbiddenException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public ForbiddenException(String reason, Throwable t) {
		super(HttpStatus.FORBIDDEN, ErrorCodes.FORBIDDEN, reason, t);
	}

}
