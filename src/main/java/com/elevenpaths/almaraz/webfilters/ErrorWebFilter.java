/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.elevenpaths.almaraz.context.RequestContext;
import com.elevenpaths.almaraz.exceptions.ResponseException;
import com.elevenpaths.almaraz.exceptions.ServerException;
import com.elevenpaths.almaraz.logging.ReactiveLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Reactive {@link WebFilter} to log the request and response with contextual information.
 * The contextual information is stored as a {@link RequestContext} instance in the
 * reactive context.
 *
 * Note that errors are not logged by this web filter. This is delegated to the error handler
 * so that it is possible to log not only the exception but also the response status code. The
 * error handler is responsible for converting an exception into a HTTP response.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ErrorWebFilter implements WebFilter {

	/**
	 * Logger.
	 */
	private static final Logger log = LoggerFactory.getLogger(ErrorWebFilter.class);

	/**
	 * Web filter implementation to write a log entry when the request is received and
	 * another log entry when the response is completed.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange)
				.doOnEach(ReactiveLogger.logOnError(t -> {
					if (t instanceof ResponseException || t instanceof ResponseStatusException) {
						log.debug("Captured handled exception", t);
					} else {
						log.error("Captured unhandled exception", t);
					}
				}))
				.onErrorResume(t -> buildErrorResponse(exchange, t));
	}

	/**
	 * Convert the exception into a {@link ResponseException} and render it as
	 * a body response.
	 *
	 * @param exchange
	 * @param t
	 * @return
	 */
	protected Mono<Void> buildErrorResponse(ServerWebExchange exchange, Throwable t) {
		ResponseException e = getResponseException(t);
		HttpStatus status = e.getStatus();
		Map<String, String> headers = e.getHeaders();
		byte[] bodyBytes = null;
		if (e.getError() != null) {
			try {
				bodyBytes = new ObjectMapper().writeValueAsBytes(e);
			} catch (JsonProcessingException e1) {
				log.error("Error marshalling exception", e1);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				headers = null;
			}
		}
		return renderErrorResponse(exchange, status, bodyBytes, headers);
	}

	/**
	 * Render a body response with status, body (as byte array) and headers.
	 *
	 * @param exchange
	 * @param status
	 * @param bodyBytes
	 * @param headers
	 * @return
	 */
	protected Mono<Void> renderErrorResponse(
			ServerWebExchange exchange, HttpStatus status, byte[] bodyBytes, Map<String, String> headers) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				response.getHeaders().add(entry.getKey(), entry.getValue());
			}
		}
		if (bodyBytes == null) {
			return Mono.empty();
		}
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		DataBuffer buffer = response.bufferFactory().wrap(bodyBytes);
		return response.writeWith(Mono.just(buffer));
	}

	/**
	 * Map the exception to a {@link ResponseException} so that it is possible to render
	 * the error response.
	 * The {@link ResponseStatusException} already contains the status code for the error.
	 * Unhandled exceptions are mapped to {@link ServerException}.
	 *
	 * @param t
	 * @return
	 */
	protected ResponseException getResponseException(final Throwable t) {
		if (t instanceof ResponseException) {
			return (ResponseException) t;
		}
		if (t instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException)t;
			return new ResponseException(responseStatusException.getStatus());
		}
		return new ServerException(t);
	}

}
