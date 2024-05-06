/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.elevenpaths.almaraz.context.RequestContext;
import com.elevenpaths.almaraz.utils.LoggingAppender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * Unit tests for {@link LoggerWebFilter}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class LoggerWebFilterTest {

	@Test
	public void filter() throws UnknownHostException, InterruptedException {
		LoggerWebFilter filter = new LoggerWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain();
		MockServerHttpRequest request = MockServerHttpRequest
				.get("/api/test")
				.remoteAddress(new InetSocketAddress(InetAddress.getByName("172.16.1.11"), 1000))
				.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		RequestContext requestContext = new RequestContext()
				.setCorrelator("test-corr")
				.setTransactionId("test-trans");

		LoggingAppender.clearEvents();

		filter.filter(exchange, chain)
			.contextWrite(Context.of(RequestContext.class, requestContext))
			.block(Duration.ZERO);

		List<ILoggingEvent> events = LoggingAppender.getEvents();
		Assertions.assertEquals(2, events.size());
		Assertions.assertEquals("Request", events.get(0).getMessage());
		Assertions.assertEquals("Response", events.get(1).getMessage());
	}

	private static class TestWebFilterChain implements WebFilterChain {

		@Override
		public Mono<Void> filter(ServerWebExchange exchange) {
			return exchange.getResponse().setComplete();
		}

	}

}
