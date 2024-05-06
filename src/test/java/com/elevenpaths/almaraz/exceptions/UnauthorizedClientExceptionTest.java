/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link UnauthorizedClientException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class UnauthorizedClientExceptionTest {

	@Test
	public void newUnauthorizedClientException() {
		UnauthorizedClientException e = new UnauthorizedClientException("reason");
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		Assertions.assertEquals(ErrorCodes.UNAUTHORIZED_CLIENT, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
