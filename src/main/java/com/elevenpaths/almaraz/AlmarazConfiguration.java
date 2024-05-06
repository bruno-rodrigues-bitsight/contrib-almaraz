/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.WebFilter;

import com.elevenpaths.almaraz.context.RequestContext;
import com.elevenpaths.almaraz.context.aspects.OperationRequestContextAspect;
import com.elevenpaths.almaraz.resolvers.ValidRequestBody;
import com.elevenpaths.almaraz.resolvers.ValidRequestBodyResolver;
import com.elevenpaths.almaraz.validation.JsonSchemaRepository;
import com.elevenpaths.almaraz.validation.JsonSchemaValidator;
import com.elevenpaths.almaraz.webfilters.BasePathWebFilter;
import com.elevenpaths.almaraz.webfilters.CompleteLocationHeaderWebFilter;
import com.elevenpaths.almaraz.webfilters.ErrorWebFilter;
import com.elevenpaths.almaraz.webfilters.LoggerWebFilter;
import com.elevenpaths.almaraz.webfilters.RequestContextWebFilter;
import com.elevenpaths.almaraz.webfilters.VersionWebFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Default configuration for WebFlux applications that provides some of the Almaraz
 * components as Spring beans for dependency injection.
 *
 * It configures the WebFilter middlewares (in brackets, the order or execution
 * of the filter:
 *
 * <ul>
 * <li>RequestContextWebFilter (10)</li>
 * <li>LoggerWebFilter (20)</li>
 * <li>ErrorWebFilter (30)</li>
 * <li>CompleteLocationHeaderWebFilter (40)</li>
 * <li>BasePathWebFilter (50)</li>
 * </ul>
 *
 * It also creates the bean {@link JsonSchemaValidator} to validate against JSON schemas.
 * It configures the custom resolver {@link ValidRequestBodyResolver} to validate and bind
 * a request body to an entity class using the decorator {@link ValidRequestBody} in a
 * controller.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
@Configuration
public class AlmarazConfiguration implements WebFluxConfigurer {

	/**
	 * Base path for the REST API. It is used by {@link CompleteLocationHeaderWebFilter}.
	 */
	private final String basePath;

	/**
	 * {@link ObjectMapper} to marshal and unmarshal JSON objects with spring customization.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * Information for the application version (used by {@link VersionWebFilter}).
	 */
	private final BuildProperties buildProperties;

	/**
	 * JSON schema validation.
	 */
	private final JsonSchemaValidator validator;

	/**
	 * Constructor.
	 *
	 * @param basePath
	 */
	public AlmarazConfiguration(String basePath) {
		this(basePath, null);
	}

	/**
	 * Constructor.
	 *
	 * @param basePath
	 * @param objectMapper
	 */
	public AlmarazConfiguration(String basePath, ObjectMapper objectMapper) {
		this(basePath, objectMapper, null);
	}

	/**
	 * Constructor.
	 *
	 * @param basePath
	 * @param buildProperties
	 */
	public AlmarazConfiguration(String basePath, ObjectMapper objectMapper, BuildProperties buildProperties) {
		this.basePath = basePath;
		this.objectMapper = (objectMapper == null) ? new ObjectMapper() : objectMapper;
		this.buildProperties = buildProperties;
		validator = new JsonSchemaValidator(new JsonSchemaRepository());
	}

	/**
	 * Get the JSON Schema validator.
	 *
	 * @return JsonSchemaValidator
	 */
	@Bean
	public JsonSchemaValidator getJsonSchemaValidator() {
		return validator;
	}
	
	/**
	 * Get an instance of OperationRequestContext aspect.
	 *
	 * @return OperationRequestContextAspect
	 */
	@Bean
	public OperationRequestContextAspect getOperationRequestContextAspect() {
		return new OperationRequestContextAspect();
	}

	/**
	 * Get the {@link RequestContextWebFilter} that generates the reactive context
	 * and initializes it with the correlator and transactionId.
	 *
	 * @return {@link WebFilter} to initialize the reactive context with {@link RequestContext}.
	 */
	@Order(5)
	@Bean
	public VersionWebFilter getVersionWebFilter() {
		if (buildProperties == null) {
			return null;
		}
		return new VersionWebFilter(objectMapper, buildProperties);
	}

	/**
	 * Get the {@link RequestContextWebFilter} that generates the reactive context
	 * and initializes it with the correlator and transactionId.
	 *
	 * @return {@link WebFilter} to initialize the reactive context with {@link RequestContext}.
	 */
	@Order(10)
	@Bean
	public RequestContextWebFilter getContextWebFilter() {
		return new RequestContextWebFilter();
	}

	/**
	 * Get the {@link LoggerWebFilter} that logs the request and response.
	 *
	 * @return {@link WebFilter} to log the request and response.
	 */
	@Order(20)
	@Bean
	public LoggerWebFilter getLoggerWebFilter() {
		return new LoggerWebFilter();
	}

	/**
	 * Get the {@link ErrorWebFilter} that handles exceptions to generate an
	 * error response.
	 *
	 * @return {@link WebFilter} to handle errors.
	 */
	@Order(30)
	@Bean
	public ErrorWebFilter getErrorWebFilter() {
		return new ErrorWebFilter(objectMapper);
	}

	/**
	 * Get the {@link CompleteLocationHeaderWebFilter} that updates the location header
	 * if it is a relative path.
	 *
	 * @return {@link WebFilter} to complete the location HTTP header.
	 */
	@Order(40)
	@Bean
	public CompleteLocationHeaderWebFilter getCompleteLocationHeaderWebFilter() {
		return new CompleteLocationHeaderWebFilter();
	}

	/**
	 * Get {@link BasePathWebFilter} to configure a {@link #basePath} to the REST resources.
	 *
	 * @return {@link WebFilter} to configure an API base path (also know as context path).
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
				new ValidRequestBodyResolver(validator, objectMapper));
	}

}
