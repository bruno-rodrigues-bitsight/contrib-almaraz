/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.elevenpaths.almaraz.exceptions.NotFoundException;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit tests for {@link BasePathWebFilter}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class BasePathWebFilterTest {

	@Test
	public void filterWithBasePath() {
		BasePathWebFilter filter = new BasePathWebFilter("/api");
		TestWebFilterChain chain = new TestWebFilterChain();
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);
		Assert.assertEquals("/test", chain.path);
	}

	@Test
	public void filterWithoutBasePath() {
		BasePathWebFilter filter = new BasePathWebFilter("/api");
		TestWebFilterChain filterChain = new TestWebFilterChain();
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/other/test"));

		StepVerifier.create(filter.filter(exchange, filterChain))
			.consumeErrorWith(error -> {
				Assert.assertEquals(NotFoundException.class, error.getClass());
			})
			.verify();
	}

	private static class TestWebFilterChain implements WebFilterChain {

		private String path;

		@Override
		public Mono<Void> filter(ServerWebExchange exchange) {
			path = exchange.getRequest().getPath().pathWithinApplication().value();
			return Mono.empty();
		}

	}

}
