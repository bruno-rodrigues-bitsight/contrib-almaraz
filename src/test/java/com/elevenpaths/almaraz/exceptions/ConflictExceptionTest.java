/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link ConflictException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ConflictExceptionTest {

	@Test
	public void newConflictException() {
		ConflictException e = new ConflictException();
		Assertions.assertEquals(HttpStatus.CONFLICT, e.getStatus());
		Assertions.assertEquals(ErrorCodes.CONFLICT, e.getError());
		Assertions.assertNull(e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}
}
