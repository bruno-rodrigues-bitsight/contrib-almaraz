/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webclientfilters;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import com.elevenpaths.almaraz.context.RequestContext;
import com.elevenpaths.almaraz.utils.LoggingAppender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * Unit tests for {@link LoggerWebClientFilter}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class LoggerWebClientFilterTest {

	@Test
	public void filter() {
		RequestContext requestContext = new RequestContext()
				.setCorrelator("test-corr");

		ClientRequest request = ClientRequest.create(HttpMethod.GET, URI.create("http://localhost:8080")).build();
		ClientResponse response = Mockito.mock(ClientResponse.class);
		ExchangeFunction exchange = r -> {
			return Mono.just(response);
		};

		LoggingAppender.clearEvents();

		LoggerWebClientFilter filter = new LoggerWebClientFilter();
		ClientResponse actualResponse = filter.filter(request, exchange)
				.contextWrite(Context.of(RequestContext.class, requestContext))
				.block();
		Assertions.assertEquals(response, actualResponse);

		List<ILoggingEvent> events = LoggingAppender.getEvents();
		Assertions.assertEquals(2, events.size());
		Assertions.assertEquals("Client request", events.get(0).getMessage());
		Assertions.assertEquals("Client response", events.get(1).getMessage());
	}

}
