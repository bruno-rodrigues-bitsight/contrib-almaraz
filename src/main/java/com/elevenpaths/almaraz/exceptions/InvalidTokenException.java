/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Client/Application requested a protected OAuth resource with an invalid token.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidTokenException extends ResponseException {

	private static final long serialVersionUID = -6480648444981981820L;

	/**
	 * WWW-authenticate header for expired token.
	 */
	public static final String WWW_AUTHENTICATE_VALUE = "Bearer realm=\"cybersecurity\", error=\"invalid_token\", error_description=\"Invalid access token\"";

	/**
	 * Constructor.
	 */
	public InvalidTokenException() {
		super(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED_CLIENT, "invalid token: invalid access token");
		addHeader(HttpHeaders.WWW_AUTHENTICATE, WWW_AUTHENTICATE_VALUE);
	}
}
