/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Unit tests for {@link VersionWebFilter}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class VersionWebFilterTest {

	@Test
	public void filterWithNotVersionRequest() {
		BuildProperties buildProperties = Mockito.mock(BuildProperties.class);
		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);

		VersionWebFilter filter = new VersionWebFilter(objectMapper, buildProperties);
		TestWebFilterChain chain = new TestWebFilterChain();

		List<MockServerWebExchange> exchanges = Arrays.asList(
				MockServerWebExchange.from(MockServerHttpRequest.get("/")),
				MockServerWebExchange.from(MockServerHttpRequest.get("/version2")),
				MockServerWebExchange.from(MockServerHttpRequest.get("/version/wrong")),
				MockServerWebExchange.from(MockServerHttpRequest.post("/version")),
				MockServerWebExchange.from(MockServerHttpRequest.put("/version")),
				MockServerWebExchange.from(MockServerHttpRequest.delete("/version")),
				MockServerWebExchange.from(MockServerHttpRequest.patch("/version")));
		for (MockServerWebExchange exchange : exchanges) {
			filter.filter(exchange, chain).block(Duration.ZERO);
			Assertions.assertTrue(exchange.getResponse().isCommitted());
		}
	}

	@Test
	public void filterWithVersionRequest() throws JsonProcessingException {
		String body = "{\"version\": \"1.0\"}";

		BuildProperties buildProperties = Mockito.mock(BuildProperties.class);
		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
		Mockito.when(objectMapper.writeValueAsBytes(buildProperties)).thenReturn(body.getBytes());

		VersionWebFilter filter = new VersionWebFilter(objectMapper, buildProperties);
		TestWebFilterChain chain = new TestWebFilterChain();
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/version"));

		filter.filter(exchange, chain).block(Duration.ZERO);
		Assertions.assertEquals(HttpStatus.OK, exchange.getResponse().getStatusCode());
		Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, exchange.getResponse().getHeaders().getContentType().toString());
		Assertions.assertEquals(body, exchange.getResponse().getBodyAsString().block(Duration.ZERO));
	}

	private static class TestWebFilterChain implements WebFilterChain {
		@Override
		public Mono<Void> filter(ServerWebExchange exchange) {
			return exchange.getResponse().setComplete();
		}
	}

}
