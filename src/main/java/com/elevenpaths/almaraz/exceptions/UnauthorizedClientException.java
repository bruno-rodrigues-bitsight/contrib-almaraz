/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * This client is not authorized to use the requested grant type.
 * For example, if you restrict which applications can use the implicit grant,
 * you would return this error for the other applications.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class UnauthorizedClientException extends ResponseException {

	private static final long serialVersionUID = -7779444687270797377L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public UnauthorizedClientException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public UnauthorizedClientException(String reason, Throwable t) {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.UNAUTHORIZED_CLIENT, reason, t);
	}

}
