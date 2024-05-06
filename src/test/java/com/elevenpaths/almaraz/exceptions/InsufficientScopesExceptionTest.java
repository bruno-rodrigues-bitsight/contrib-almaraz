/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link InsufficientScopesException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class InsufficientScopesExceptionTest {

	@Test
	public void newInsufficientScopesException() {
		InsufficientScopesException e = new InsufficientScopesException();
		Assertions.assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
		Assertions.assertEquals(ErrorCodes.UNAUTHORIZED_CLIENT, e.getError());
		Assertions.assertEquals("invalid token: insufficient scopes", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertEquals(InsufficientScopesException.WWW_AUTHENTICATE_VALUE,
				e.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
		Assertions.assertNull(e.getDetailMap());
	}

}
