/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link NotFoundException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class NotFoundExceptionTest {

	@Test
	public void newNotFoundException() {
		NotFoundException e = new NotFoundException();
		Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		Assertions.assertNull(e.getError());
		Assertions.assertNull(e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}

	@Test
	public void newNotFoundExceptionWithReason() {
		NotFoundException e = new NotFoundException("reason");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		Assertions.assertEquals(ErrorCodes.NOT_FOUND, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}

	@Test
	public void newNotFoundExceptionWithReasonAndException() {
		Throwable t = new Exception("test");
		NotFoundException e = new NotFoundException("reason", t);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		Assertions.assertEquals(ErrorCodes.NOT_FOUND, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertEquals(t, e.getCause());
	}
}
