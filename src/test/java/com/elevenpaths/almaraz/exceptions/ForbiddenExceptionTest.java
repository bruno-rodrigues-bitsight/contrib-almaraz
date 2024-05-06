/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link ForbiddenException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ForbiddenExceptionTest {

	@Test
	public void newForbiddenException() {
		ForbiddenException e = new ForbiddenException();
		Assertions.assertEquals(HttpStatus.FORBIDDEN, e.getStatus());
		Assertions.assertEquals(ErrorCodes.FORBIDDEN, e.getError());
		Assertions.assertNull(e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
