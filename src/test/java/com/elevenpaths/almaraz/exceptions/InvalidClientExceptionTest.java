/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link InvalidClientException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidClientExceptionTest {

	@Test
	public void newConflictException() {
		InvalidClientException e = new InvalidClientException("reason");
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
		Assertions.assertEquals(ErrorCodes.INVALID_CLIENT, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
