/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exceptions for invalid requests.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidRequestException extends ResponseException {

	private static final long serialVersionUID = 8064181640808607578L;

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public InvalidRequestException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public InvalidRequestException(String reason, Throwable t) {
		super(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_REQUEST, reason, t);
	}

}
