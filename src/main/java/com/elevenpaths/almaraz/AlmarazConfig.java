/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

import com.elevenpaths.almaraz.resolvers.ValidRequestBodyResolver;
import com.elevenpaths.almaraz.validation.JsonSchemaRepository;
import com.elevenpaths.almaraz.validation.JsonSchemaValidator;
import com.elevenpaths.almaraz.webfilters.BasePathWebFilter;
import com.elevenpaths.almaraz.webfilters.CompleteLocationHeaderWebFilter;
import com.elevenpaths.almaraz.webfilters.ErrorWebFilter;
import com.elevenpaths.almaraz.webfilters.LoggerWebFilter;
import com.elevenpaths.almaraz.webfilters.RequestContextWebFilter;

/**
 * Default configuration for WebFlux applications that provides some of the Almaraz
 * components as Spring beans for dependency injection.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class AlmarazConfig implements WebFluxConfigurer {

	/**
	 * Base path for the REST API. It is used by {@link CompleteLocationHeaderWebFilter}.
	 */
	private final String basePath;

	/**
	 * JSON schema validation.
	 */
	private final JsonSchemaValidator validator;

	/**
	 * Constructor.
	 *
	 * @param basePath
	 */
	public AlmarazConfig(String basePath) {
		this.basePath = basePath;
		validator = new JsonSchemaValidator(new JsonSchemaRepository());
	}

	/**
	 * Get the JSON Schema validator.
	 *
	 * @return
	 */
	@Bean
	public JsonSchemaValidator getJsonSchemaValidator() {
		return validator;
	}

	/**
	 * Get the {@link RequestContextWebFilter} that generates the reactive context
	 * and initializes it with the correlator and transactionId.
	 *
	 * @return
	 */
	@Order(10)
	@Bean
	public RequestContextWebFilter getContextWebFilter() {
		return new RequestContextWebFilter();
	}

	@Order(20)
	@Bean
	public LoggerWebFilter getLoggerWebFilter() {
		return new LoggerWebFilter();
	}

	/**
	 * Get the {@link ErrorWebFilter} that handles exceptions to generate an
	 * error response.
	 *
	 * @return
	 */
	@Order(30)
	@Bean
	public ErrorWebFilter getErrorWebFilter() {
		return new ErrorWebFilter();
	}

	/**
	 * Get the {@link CompleteLocationHeaderWebFilter} that updates the location header
	 * if it is a relative path.
	 *
	 * @return
	 */
	@Order(40)
	@Bean
	public CompleteLocationHeaderWebFilter getCompleteLocationHeaderWebFilter() {
		return new CompleteLocationHeaderWebFilter();
	}

	/**
	 * Get {@link BasePathWebFilter} to configure a {@link #basePath} to the REST resources.
	 *
	 * @return
	 */
	@Order(50)
	@Bean
	public BasePathWebFilter getBasePathWebFilter() {
		return new BasePathWebFilter(basePath);
	}

	/**
	 * Configure the {@link ValidRequestBodyResolver} annotation to validate arguments
	 * against a JSON schema.
	 */
	@Override
	public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
		WebFluxConfigurer.super.configureArgumentResolvers(configurer);
		configurer.addCustomResolver(
				new ValidRequestBodyResolver(validator));
	}

}
