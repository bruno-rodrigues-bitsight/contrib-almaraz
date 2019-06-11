/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * The authorization code (or user’s password for the password grant type) is invalid or expired.
 * This is also the error you would return if the redirect URL given in the authorization grant
 * does not match the URL provided in this access token request.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidGrantException extends ResponseException {

	private static final long serialVersionUID = -2711327471897957202L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public InvalidGrantException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public InvalidGrantException(String reason, Throwable t) {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_GRANT, reason, t);
	}

}
