// Copyright (c) Telefonica I+D. All rights reserved.

package com.elevenpaths.almaraz.handlers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;
import org.springframework.web.server.ResponseStatusException;

import com.elevenpaths.almaraz.exceptions.ResponseException;
import com.elevenpaths.almaraz.exceptions.ServerException;

import reactor.core.publisher.Mono;

/**
 * Spring webflux handler to intercept exceptions and generate a custom response error.
 *
 * The errors follow the same format as OAuth standard. It is a JSON document with two fields:
 * <ul>
 * <li><b>error</b>. Error identifier. This is constrained to a few values to categorize the error.
 * For example, server_error or invalid_request.</li>
 * <li><b>error_description</b>. Description of the error.</li>
 * </ul>
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class ErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ErrorWebExceptionHandler.class);

	/**
	 * Constructor.
	 *
	 * @param errorAttributes
	 * @param resourceProperties
	 * @param applicationContext
	 * @param serverCodecConfigurer
	 */
	public ErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
		super(errorAttributes, resourceProperties, applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	/**
	 * Configure the {@link RouterFunction} to handle every error, independently of the path,
	 * with {@link #renderErrorResponse(ServerRequest)}.
	 */
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	/**
	 * Generate a response error with JSON format following the OAuth standard format
	 * for errors. The response error is marshalled with {@link ResponseException} using
	 * Jackson.
	 *
	 * @param request
	 * @return
	 */
	protected Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {
		ResponseException e = getResponseStatusException(request);
		BodyBuilder bodyBuilder = ServerResponse.status(e.getStatus());
		if (e.getHeaders() != null) {
			e.getHeaders().forEach(bodyBuilder::header);
		}
		if (e.getError() != null) {
			bodyBuilder.contentType(MediaType.APPLICATION_JSON_UTF8);
			return bodyBuilder.body(BodyInserters.fromObject(e));
		}
		return bodyBuilder.body(BodyInserters.empty());
	}

	/**
	 * Map the exception to a {@link ResponseException} so that it is possible to render
	 * the error response.
	 * The {@link ResponseStatusException} already contains the status code for the error.
	 * Unhandled exceptions are mapped to {@link ServerException}.
	 *
	 * @param request
	 * @return
	 */
	protected ResponseException getResponseStatusException(final ServerRequest request) {
		Throwable t = getError(request);
		if (t instanceof ResponseException) {
			return (ResponseException) t;
		}
		if (t instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException)t;
			return new ResponseException(responseStatusException.getStatus());
		}
		LOGGER.warn("Handling unexpected exception type", t);
		return new ServerException(t);
	}

}
