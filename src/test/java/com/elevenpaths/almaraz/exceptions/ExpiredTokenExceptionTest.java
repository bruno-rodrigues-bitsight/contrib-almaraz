/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link ExpiredTokenException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ExpiredTokenExceptionTest {

	@Test
	public void newExpiredTokenException() {
		ExpiredTokenException e = new ExpiredTokenException();
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		Assertions.assertEquals(ErrorCodes.UNAUTHORIZED_CLIENT, e.getError());
		Assertions.assertEquals("invalid token: expired token", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertEquals(ExpiredTokenException.WWW_AUTHENTICATE_VALUE,
				e.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
		Assertions.assertNull(e.getDetailMap());
	}

}
