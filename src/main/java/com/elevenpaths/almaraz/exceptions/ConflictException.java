/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;


/**
 * Conflict (409 response).
 *
 * @author Juan Antonio Hernando <juanantonio.hernandolabajo@telefonica.com>
 *
 */
public class ConflictException extends ResponseException {

	private static final long serialVersionUID = 564052190751854075L;

	/**
	 * Default constructor.
	 */
	public ConflictException() {
		super(HttpStatus.CONFLICT, null, null);
	}

	/**
	 * Constructor with error reason.
	 *
	 * @param reason reason
	 */
	public ConflictException(String reason) {
		this(reason, null);
	}

	/**
	 * Constructor with error reason and exception.
	 *
	 * @param reason reason
	 * @param t exception
	 */
	public ConflictException(String reason, Throwable t) {
		super(HttpStatus.CONFLICT, ErrorCodes.CONFLICT, reason, t);
	}

}
