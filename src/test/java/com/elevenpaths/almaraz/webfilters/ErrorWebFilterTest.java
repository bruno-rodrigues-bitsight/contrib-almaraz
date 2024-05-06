/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.elevenpaths.almaraz.exceptions.InsufficientScopesException;
import com.elevenpaths.almaraz.exceptions.InvalidRequestException;
import com.elevenpaths.almaraz.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit tests for {@link ErrorWebFilter}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ErrorWebFilterTest {

	@Test
	public void filterWithNotFoundException() {
		ErrorWebFilter filter = new ErrorWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain(new NotFoundException());
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, exchange.getResponse().getStatusCode());
	}

	@Test
	public void filterWithInvalidRequestException() {
		ErrorWebFilter filter = new ErrorWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain(new InvalidRequestException("$.country is invalid"));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);

		Assertions.assertEquals(HttpStatus.BAD_REQUEST, exchange.getResponse().getStatusCode());
		Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, exchange.getResponse().getHeaders().getContentType().toString());
		validateErrorBody(exchange, "invalid_request", "$.country is invalid");
	}

	@Test
	public void filterWithInsufficientScopesException() {
		ErrorWebFilter filter = new ErrorWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain(new InsufficientScopesException());
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);

		Assertions.assertEquals(HttpStatus.FORBIDDEN, exchange.getResponse().getStatusCode());
		Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, exchange.getResponse().getHeaders().getContentType().toString());
		Assertions.assertEquals(InsufficientScopesException.WWW_AUTHENTICATE_VALUE, exchange.getResponse().getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE));
		validateErrorBody(exchange, "unauthorized_client", "invalid token: insufficient scopes");
	}

	@Test
	public void filterWithResponseStatusException() {
		ErrorWebFilter filter = new ErrorWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain(new ResponseStatusException(HttpStatus.NOT_FOUND));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, exchange.getResponse().getStatusCode());
	}

	@Test
	public void filterWithUnhandledException() {
		ErrorWebFilter filter = new ErrorWebFilter();
		TestWebFilterChain chain = new TestWebFilterChain(new Exception("Unhandled exception"));
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test"));

		filter.filter(exchange, chain).block(Duration.ZERO);

		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exchange.getResponse().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	private void validateErrorBody(MockServerWebExchange exchange, String expectedError, String expectedErrorDescription) {
		StepVerifier.create(exchange.getResponse().getBodyAsString())
			.consumeNextWith(body -> {
				try {
					Map<String, String> errorMap = new ObjectMapper().readValue(body, Map.class);
					Assertions.assertEquals(expectedError, errorMap.get("error"));
					Assertions.assertEquals(expectedErrorDescription, errorMap.get("error_description"));
				} catch (IOException e) {
					Assertions.fail();
				}
			})
			.verifyComplete();
	}

	private static class TestWebFilterChain implements WebFilterChain {

		private final Throwable t;

		public TestWebFilterChain(Throwable t) {
			this.t = t;
		}

		@Override
		public Mono<Void> filter(ServerWebExchange exchange) {
			return Mono.error(t);
		}

	}

}
