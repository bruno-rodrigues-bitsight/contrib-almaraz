/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */
package com.elevenpaths.almaraz.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;


/**
 * Unit tests for {@link RequestContext}.
 *
 * @author Juan Hernando <juanantonio.hernandolabajo@telefonica.com>
 *
 */
public class RequestContextTest {

	@Test
	public void requestContextFilled() {
		RequestContext requestContext = new RequestContext();
		requestContext.setTransactionId("transId");
		requestContext.setCorrelator("corr");
		requestContext.setOperation("op");
		requestContext.setService("srv");
		requestContext.setComponent("comp");
		requestContext.setUser("user");
		requestContext.setRealm("realm");

		requestContext.put("customStr", "custom");
		requestContext.put("customLong", 1L);
		requestContext.put("customLong2", (Long) null);
		requestContext.put("customBool", true);
		requestContext.put("customBool2", (Boolean) null);

		assertEquals("transId", requestContext.getTransactionId());
		assertEquals("corr", requestContext.getCorrelator());
		assertEquals("op", requestContext.getOperation());
		assertEquals("srv", requestContext.getService());
		assertEquals("comp", requestContext.getComponent());
		assertEquals("user", requestContext.getUser());
		assertEquals("realm", requestContext.getRealm());

		Map<String, String> context = requestContext.getContextMap();
		assertNotNull(context);

		assertEquals("custom", requestContext.getString("customStr"));
		assertEquals(Long.valueOf(1L), requestContext.getLong("customLong"));
		assertNull(requestContext.getLong("customLong2"));
		assertEquals(true, requestContext.getBoolean("customBool"));
		assertFalse(requestContext.getBoolean("customBool2"));
		assertFalse(requestContext.getBoolean("customStr"));
	}

	@Test
	public void requestContextReactiveStream() {
		RequestContext requestContext = new RequestContext();
		requestContext.setTransactionId("transId");

		StepVerifier.create(RequestContext.context())
		.assertNext(rc -> "transId".equals(rc.getTransactionId())).verifyComplete();
	}

}
