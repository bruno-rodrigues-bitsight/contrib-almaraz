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
public class UnsupportedGrantTypeExceptionTest {

	@Test
	public void newUnsupportedGrantTypeException() {
		UnsupportedGrantTypeException e = new UnsupportedGrantTypeException("reason");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		Assertions.assertEquals(ErrorCodes.UNSUPPORTED_GRANT_TYPE, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
