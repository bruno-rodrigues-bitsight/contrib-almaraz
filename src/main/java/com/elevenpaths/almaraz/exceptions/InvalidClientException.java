/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Client authentication failed, such as if the request contains invalid credentials.
 * Send an HTTP 401 response in this case.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidClientException extends ResponseException {

	private static final long serialVersionUID = -927969639328057586L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public InvalidClientException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public InvalidClientException(String reason, Throwable t) {
		super(HttpStatus.UNAUTHORIZED, ErrorCodes.INVALID_CLIENT, reason, t);
	}

}
