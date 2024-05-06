/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */
package com.elevenpaths.almaraz.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

/**
 * Unit tests for {@link MDCServerWebExchange}.
 *
 * @author Juan Hernando <juanantonio.hernandolabajo@telefonica.com>
 *
 */
@ExtendWith(MockitoExtension.class)
public class MDCServerWebExchangeTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private ServerWebExchange exchange;

	@Mock
	private ServerHttpResponse responseMock;

	@AfterEach
	public void reset_mocks() {
		Mockito.reset(exchange, responseMock);
	}

	@Test
	public void getMethodTest() {
		Mockito.when(exchange.getRequest().getMethod().toString()).then(answer-> {
			return "test";
		});
		String result = MDCServerWebExchange.getMethod(exchange);
		assertNotNull(result);
		assertEquals("test", result);
	}

	@Test
	public void getMethodExceptionTest() {
		Mockito.when(exchange.getRequest().getMethod().toString()).then(answer-> {
			throw new Exception("error");
		});
		String result = MDCServerWebExchange.getMethod(exchange);
		assertNull(result);
	}

	@Test
	public void getPathTest() throws URISyntaxException {
		URI uri = new URI("http://test.test/path/test?key=test&key2=test2");
		Mockito.when(exchange.getRequest().getURI()).then(answer-> {
			return uri;
		});
		String result = MDCServerWebExchange.getPath(exchange);
		assertNotNull(result);
		assertEquals("/path/test", result);
	}

	@Test
	public void getPathExceptionTest() {
		Mockito.when(exchange.getRequest().getURI()).then(answer-> {
			throw new Exception("error");
		});
		String result = MDCServerWebExchange.getPath(exchange);
		assertNull(result);
	}

	@Test
	public void getQueryParamsTest() throws URISyntaxException {
		URI uri = new URI("http://test.test/path/test?key=test&key2=test2");
		Mockito.when(exchange.getRequest().getURI()).then(answer-> {
			return uri;
		});
		String result = MDCServerWebExchange.getQueryParams(exchange);
		assertNotNull(result);
		assertEquals("key=test&key2=test2", result);
	}

	@Test
	public void getQueryParamsExceptionTest() {
		Mockito.when(exchange.getRequest().getURI()).then(answer-> {
			throw new Exception("error");
		});
		String result = MDCServerWebExchange.getQueryParams(exchange);
		assertNull(result);
	}

	@Test
	public void getRemoteAddressTest() {
		InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
		Mockito.when(exchange.getRequest().getRemoteAddress()).then(answer-> {
			return inetSocketAddress;
		});

		String result = MDCServerWebExchange.getRemoteAddress(exchange);
		assertNotNull(result);
		assertEquals("localhost/127.0.0.1", result);
	}

	@Test
	public void getRemoteAddressFromXFFTest() {
		Mockito.when(exchange.getRequest().getHeaders()).then(answer-> {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Forwarded-For", "127.0.0.2,127.0.0.1");
			return headers;
		});

		String result = MDCServerWebExchange.getRemoteAddress(exchange);
		assertNotNull(result);
		assertEquals("127.0.0.2", result);
	}

	@Test
	public void getRemoteAddressExceptionTest() {
		Mockito.when(exchange.getRequest().getRemoteAddress()).then(answer-> {
			throw new Exception("error");
		});
		String result = MDCServerWebExchange.getRemoteAddress(exchange);
		assertNull(result);
	}

	@Test
	public void getStatusCodeTest() {
		HttpStatus httpStatus = HttpStatus.CREATED;
    	Mockito.when(exchange.getResponse()).thenReturn(responseMock);
		Mockito.when(responseMock.getStatusCode()).then(answer-> {
			return httpStatus;
		});
		String result = MDCServerWebExchange.getStatusCode(exchange);
		assertNotNull(result);
		assertEquals("201", result);
	}

	@Test
	public void getStatusCodeExceptionTest() {
		Mockito.when(exchange.getResponse()).thenReturn(responseMock);
		Mockito.when(responseMock.getStatusCode()).then(answer-> {
			throw new Exception("error");
		});
		String result = MDCServerWebExchange.getStatusCode(exchange);
		assertNull(result);
	}

}
