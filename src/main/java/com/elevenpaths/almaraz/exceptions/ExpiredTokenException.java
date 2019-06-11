/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Client/Application requested a protected OAuth resource with an expired token.
 * 
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ExpiredTokenException extends ResponseException {

	private static final long serialVersionUID = -1655857519486353626L;
	
	/**
	 * WWW-authenticate header for expired token.
	 */
	public static final String WWW_AUTHENTICATE_VALUE =
			"Bearer realm=\"cybersecurity\", error=\"invalid_token\", error_description=\"expired token\"";

	/**
	 * Constructor.
	 */
	public ExpiredTokenException() {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.UNAUTHORIZED_CLIENT, "invalid token: expired token");
		addHeader(HttpHeaders.WWW_AUTHENTICATE, WWW_AUTHENTICATE_VALUE);
	}
}
