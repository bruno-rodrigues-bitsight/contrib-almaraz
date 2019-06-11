/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.webfilters;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.elevenpaths.almaraz.exceptions.NotFoundException;

import reactor.core.publisher.Mono;

/**
 * Reactive {@link WebFilter} to work with a base path with webflux.
 * The goal is to make it configurable and avoid repeating the base path in the
 * controller.
 *
 * Note that configuration property <b>server.servlet.context-path</b> is not actually working.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class BasePathWebFilter implements WebFilter {

	/**
	 * Base path.
	 */
	private final String basePath;

	/**
	 * Constructor.
	 *
	 * @param basePath
	 */
	public BasePathWebFilter(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * Implementation of the web filter that modifies the request to set up the context path
	 * with the {@link #basePath}.
	 * Note that if the request URI does not start with the {@link #basePath}, then it throws
	 * a {@link NotFoundException}.
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		try {
			ServerHttpRequest request = exchange.getRequest().mutate().contextPath(basePath).build();
			return chain.filter(exchange.mutate().request(request).build());
		} catch (IllegalArgumentException e) {
			return Mono.error(new NotFoundException());
		}
	}

}
