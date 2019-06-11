/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception for server errors.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ServerException extends ResponseException {

	private static final long serialVersionUID = -380175644901247549L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason
	 */
	public ServerException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error exception.
	 *
	 * @param t exception
	 */
	public ServerException(Throwable t) {
		this(null, t);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public ServerException(String reason, Throwable t) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.SERVER_ERROR, reason, t);
	}

}
