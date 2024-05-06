/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * Unit tests for {@link ServerException}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ServerExceptionTest {

	@Test
	public void newServerExceptionWithReason() {
		ServerException e = new ServerException("reason");
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
		Assertions.assertEquals(ErrorCodes.SERVER_ERROR, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertNull(e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}

	@Test
	public void newServerExceptionWithThrowable() {
		Throwable t = new Exception("test exception");
		ServerException e = new ServerException(t);
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
		Assertions.assertEquals(ErrorCodes.SERVER_ERROR, e.getError());
		Assertions.assertNull(e.getReason());
		Assertions.assertEquals(t, e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}

	@Test
	public void newServerExceptionWithReasonAndThrowable() {
		Throwable t = new Exception("test exception");
		ServerException e = new ServerException("reason", t);
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
		Assertions.assertEquals(ErrorCodes.SERVER_ERROR, e.getError());
		Assertions.assertEquals("reason", e.getReason());
		Assertions.assertEquals(t, e.getCause());
		Assertions.assertNull(e.getHeaders());
		Assertions.assertNull(e.getDetailMap());
	}

}
