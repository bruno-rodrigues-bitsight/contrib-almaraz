/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.UriTemplate;

import reactor.core.publisher.Mono;

/**
 * Reactive {@link WebFilter} to update the location header inserted by a controller
 * with a relative path (e.g. the identifier of a new resource created) in order to make
 * it absolute with the request URI.
 *
 * It simplifies the creation of location headers in REST APIs.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class CompleteLocationHeaderWebFilter implements WebFilter {

	/**
	 * Implementation of the web filter that modifies the response if there is a location header
	 * and it is a relative path.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		exchange.getResponse().beforeCommit(() -> {
			HttpHeaders httpHeaders = exchange.getResponse().getHeaders();
			URI uriLocation = httpHeaders.getLocation();
			if (uriLocation != null && uriLocation.getPath() != null && !uriLocation.getPath().startsWith("/")) {
				URI newUriLocation = new UriTemplate("{requestUri}/{locationUri}")
						.expand(exchange.getRequest().getURI(), httpHeaders.getLocation());
				httpHeaders.setLocation(newUriLocation);
			}
			return Mono.empty();
		});
		return chain.filter(exchange);
	}
}
