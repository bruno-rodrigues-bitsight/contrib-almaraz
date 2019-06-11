/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Exception when a client tries to access a protected OAuth resource with a valid access token but the
 * access is forbidden because the required scopes are not fulfilled by the token.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InsufficientScopesException extends ResponseException {

	private static final long serialVersionUID = -8973741440991297788L;

	/**
	 * WWW-authenticate header for insufficient scopes.
	 */
	public static final String WWW_AUTHENTICATE_VALUE =
			"Bearer realm=\"cybersecurity\", error=\"insufficient_scope\", error_description=\"Insufficient scopes\"";

	/**
	 * Constructor.
	 */
	public InsufficientScopesException() {
		super(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED_CLIENT, "invalid token: insufficient scopes");
		addHeader(HttpHeaders.WWW_AUTHENTICATE, WWW_AUTHENTICATE_VALUE);
	}
}
