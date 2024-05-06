/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import org.springframework.boot.info.BuildProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

/**
 * Reactive {@link WebFilter} to generate a response with the version.
 *
 * The version is obtained from BuildProperties that are injected in the {@link VersionWebFilter} constructor.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class VersionWebFilter implements WebFilter {

	/**
	 * Default path where version API is available.
	 */
	public static final String DEFAULT_PATH = "/version";

	/**
	 * Information of the artifact with the version and other properties.
	 */
	private final byte[] buildPropertiesBytes;

	/**
	 * Path where version API is available. By default, /version
	 */
	private final String path;

	/**
	 * Constructor.
	 *
	 * @param objectMapper
	 * @param buildProperties
	 */
	public VersionWebFilter(ObjectMapper objectMapper, BuildProperties buildProperties) {
		this(objectMapper, buildProperties, DEFAULT_PATH);
	}

	/**
	 * Constructor.
	 *
	 * @param objectMapper
	 * @param buildProperties
	 * @param path
	 */
	public VersionWebFilter(ObjectMapper objectMapper, BuildProperties buildProperties, String path) {
		this.buildPropertiesBytes = marshalVersionResponseBody(objectMapper, buildProperties);
		this.path = path;
	}

	/**
	 * Implementation of the web filter that replies with the version of the application
	 * using the {@link BuildProperties} instance.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		if (!isGetVersion(exchange)) {
			return chain.filter(exchange);
		}
		return renderVersionResponse(exchange);
	}

	/**
	 * Convert a {@link BuildProperties} object into a byte array to be rendered in the response.
	 *
	 * @param objectMapper
	 * @param buildProperties
	 * @return buildProperties as byte array
	 */
	protected byte[] marshalVersionResponseBody(ObjectMapper objectMapper, BuildProperties buildProperties) {
		try {
			return objectMapper.writeValueAsBytes(buildProperties);
		} catch (JsonProcessingException e) {
			return new byte[] {};
		}
	}

	/**
	 * Check if the request matches with the version API.
	 *
	 * @param exchange
	 * @return true if method is GET and request path is path (by default /version)
	 */
	protected boolean isGetVersion(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		return (HttpMethod.GET.equals(request.getMethod()) && request.getPath().value().equals(path));
	}

	/**
	 * Generate a response body with the {@link BuildProperties} as JSON.
	 *
	 * @param exchange
	 * @return Mono<Void>
	 */
	protected Mono<Void> renderVersionResponse(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.OK);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		DataBuffer buffer = response.bufferFactory().wrap(buildPropertiesBytes);
		return response.writeWith(Mono.just(buffer));
	}

}
