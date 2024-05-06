/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

import com.elevenpaths.almaraz.resolvers.ValidRequestBodyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for {@link AlmarazConfiguration}.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class AlmarazConfigurationTest {

	@Test
	public void beans() {
		AlmarazConfiguration config = new AlmarazConfiguration("/api");
		Assertions.assertNotNull(config.getJsonSchemaValidator());
		Assertions.assertNotNull(config.getContextWebFilter());
		Assertions.assertNotNull(config.getLoggerWebFilter());
		Assertions.assertNotNull(config.getErrorWebFilter());
		Assertions.assertNotNull(config.getCompleteLocationHeaderWebFilter());
		Assertions.assertNotNull(config.getBasePathWebFilter());
	}

	@Test
	public void versionBean() {
		AlmarazConfiguration config = new AlmarazConfiguration("/api");
		Assertions.assertNull(config.getVersionWebFilter());

		ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
		BuildProperties buildProperties = Mockito.mock(BuildProperties.class);
		config = new AlmarazConfiguration("/api", objectMapper, buildProperties);
		Assertions.assertNotNull(config.getVersionWebFilter());
	}

	@Test
	public void configureArgumentResolvers() {
		ArgumentResolverConfigurer configurer = Mockito.mock(ArgumentResolverConfigurer.class);
		AlmarazConfiguration config = new AlmarazConfiguration("/api");
		config.configureArgumentResolvers(configurer);
		Mockito.verify(configurer).addCustomResolver(Mockito.any(ValidRequestBodyResolver.class));
	}

}
