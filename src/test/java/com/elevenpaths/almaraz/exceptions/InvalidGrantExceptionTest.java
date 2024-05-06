/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link InvalidGrantException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidGrantExceptionTest {

	@Test
	public void newInvalidGrantException() {
		InvalidGrantException e = new InvalidGrantException("reason");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		Assertions.assertEquals(ErrorCodes.INVALID_GRANT, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
