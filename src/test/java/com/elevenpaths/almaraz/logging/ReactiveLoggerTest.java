/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.logging;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.elevenpaths.almaraz.context.RequestContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

/**
 * Unit tests for {@link ReactiveLogger}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ReactiveLoggerTest {

	@Test
	public void logOnNext() {
		String result = Mono.just("test")
				.doOnEach(ReactiveLogger.logOnNext(value -> {
					Assertions.assertEquals("test", value);
				}))
				.contextWrite(Context.of(RequestContext.class, new RequestContext()))
				.block();

		Assertions.assertEquals("test", result);
	}

	@Test
	public void logOnComplete() {
		final Map<String, Boolean> infoMap = new HashMap<>();
		infoMap.put("completed", Boolean.FALSE);
		Flux.just("test1", "test2")
				.doOnEach(ReactiveLogger.logOnComplete(() -> {
					Assertions.assertFalse(infoMap.get("completed"));
					infoMap.put("completed", Boolean.TRUE);
				}))
				.contextWrite(Context.of(RequestContext.class, new RequestContext()))
				.blockLast();

		Assertions.assertTrue(infoMap.get("completed"));
	}

	@Test
	public void logOnError() {
		RuntimeException e = new RuntimeException("test error");
		Flux<String> result = Flux.just("test1", "testInvalid")
				.map(value -> {
					if (value.equals("testInvalid")) {
						throw e;
					}
					return value;
				})
				.doOnEach(ReactiveLogger.logOnError(t -> {
					Assertions.assertEquals(e, t);
				}))
				.contextWrite(Context.of(RequestContext.class, new RequestContext()));

		StepVerifier.create(result)
			.expectNextCount(1)
			.expectErrorSatisfies(error -> {
				Assertions.assertEquals(e, error);
			})
			.verify();
	}

	@Test
	public void log() {
		Map<String, Boolean> map = new HashMap<>();
		Mono<Void> result = ReactiveLogger.log(() -> {
			map.put("logged", true);
		});
		result.block();
		Assertions.assertEquals(true, map.get("logged"));
	}

}
