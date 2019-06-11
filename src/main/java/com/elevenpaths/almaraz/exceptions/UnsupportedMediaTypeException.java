/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Unsupported media type (415 response).
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class UnsupportedMediaTypeException extends ResponseException {

	private static final long serialVersionUID = 3098028308954612696L;

	/**
	 * Default constructor.
	 */
	public UnsupportedMediaTypeException() {
		super(HttpStatus.UNSUPPORTED_MEDIA_TYPE, null, null);
	}

}
