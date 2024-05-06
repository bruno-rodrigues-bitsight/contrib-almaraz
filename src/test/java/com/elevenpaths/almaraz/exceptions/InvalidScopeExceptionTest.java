/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link InvalidScopeException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidScopeExceptionTest {

	@Test
	public void newInvalidScopeException() {
		InvalidScopeException e = new InvalidScopeException("reason");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		Assertions.assertEquals(ErrorCodes.INVALID_SCOPE, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
