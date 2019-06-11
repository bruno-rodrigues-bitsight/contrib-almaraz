/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * For access token requests that include a scope (password or client_credentials grants),
 * this error indicates an invalid scope value in the request.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidScopeException extends ResponseException {

	private static final long serialVersionUID = 5163453607811923608L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public InvalidScopeException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public InvalidScopeException(String reason, Throwable t) {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_SCOPE, reason, t);
	}

}
