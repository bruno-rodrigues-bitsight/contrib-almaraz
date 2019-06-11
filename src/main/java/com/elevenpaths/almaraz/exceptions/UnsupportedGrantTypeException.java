/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * If a grant type is requested that the authorization server doesn’t recognize, use this code.
 * Note that unknown grant types also use this specific error code rather than using the invalid_request.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class UnsupportedGrantTypeException extends ResponseException {

	private static final long serialVersionUID = -8554736271466165168L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public UnsupportedGrantTypeException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public UnsupportedGrantTypeException(String reason, Throwable t) {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.UNSUPPORTED_GRANT_TYPE, reason, t);
	}

}
