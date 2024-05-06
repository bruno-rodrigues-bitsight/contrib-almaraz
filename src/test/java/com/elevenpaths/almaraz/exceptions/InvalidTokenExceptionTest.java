/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link InvalidTokenException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InvalidTokenExceptionTest {

	@Test
	public void newInvalidTokenException() {
		InvalidTokenException e = new InvalidTokenException();
		Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
		Assertions.assertEquals(ErrorCodes.UNAUTHORIZED_CLIENT, e.getError());
		Assertions.assertEquals("invalid token: invalid access token", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertEquals(InvalidTokenException.WWW_AUTHENTICATE_VALUE,
				e.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
		Assertions.assertNull(e.getDetailMap());
	}

}
