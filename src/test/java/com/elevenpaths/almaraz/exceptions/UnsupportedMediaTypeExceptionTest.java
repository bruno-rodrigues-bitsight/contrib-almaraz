/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link UnsupportedMediaTypeException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class UnsupportedMediaTypeExceptionTest {

	@Test
	public void newUnsupportedMediaTypeException() {
		UnsupportedMediaTypeException e = new UnsupportedMediaTypeException();
		Assertions.assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getStatus());
		Assertions.assertNull(e.getError());
		Assertions.assertNull(e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
