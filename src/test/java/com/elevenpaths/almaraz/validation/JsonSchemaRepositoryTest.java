/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */
package com.elevenpaths.almaraz.validation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.elevenpaths.almaraz.validation.JsonSchemaRepository.JsonSchemaRepositoryException;
import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;


/**
 * Unit tests for {@link JsonSchemaRepository}.
 *
 * @author Juan Hernando <juanantonio.hernandolabajo@telefonica.com>
 *
 */
public class JsonSchemaRepositoryTest {

	private static JsonSchemaRepository jsonSchemaRepository;

	@BeforeAll
	public static void init() {
		jsonSchemaRepository = new JsonSchemaRepository();
	}

	@Test
	public void getJsonSchemaWithRightFile() {
		JsonSchema result = jsonSchemaRepository.getJsonSchema("schema");
		Assertions.assertNotNull(result);
		JsonNode schemaNode = result.getSchemaNode();
		Assertions.assertNotNull(schemaNode);
		JsonNode firstNameNode = schemaNode.get("properties").get("firstName");
		JsonNode lastNameNode = schemaNode.get("properties").get("lastName");
		JsonNode ageNode = schemaNode.get("properties").get("age");
		Assertions.assertNotNull(firstNameNode);
		Assertions.assertEquals("string", firstNameNode.get("type").asText());
		Assertions.assertNotNull(lastNameNode);
		Assertions.assertEquals("string", lastNameNode.get("type").asText());
		Assertions.assertNotNull(ageNode);
		Assertions.assertEquals("integer", ageNode.get("type").asText());
		Assertions.assertEquals(0, ageNode.get("minimum").asInt());
	}

	@Test
	public void getJsonSchemaUseInternalHashMap() {
		JsonSchema result = jsonSchemaRepository.getJsonSchema("schema");
		Assertions.assertNotNull(result);
		JsonNode schemaNode = result.getSchemaNode();
		Assertions.assertNotNull(schemaNode);
		JsonNode firstNameNode = schemaNode.get("properties").get("firstName");
		JsonNode lastNameNode = schemaNode.get("properties").get("lastName");
		JsonNode ageNode = schemaNode.get("properties").get("age");
		Assertions.assertNotNull(firstNameNode);
		Assertions.assertEquals("string", firstNameNode.get("type").asText());
		Assertions.assertNotNull(lastNameNode);
		Assertions.assertEquals("string", lastNameNode.get("type").asText());
		Assertions.assertNotNull(ageNode);
		Assertions.assertEquals("integer", ageNode.get("type").asText());
		Assertions.assertEquals(0, ageNode.get("minimum").asInt());
	}

	@Test()
	public void getJsonSchemaServerExceptionUnknownSchema() {
		Assertions.assertThrows(JsonSchemaRepositoryException.class, () -> 
			jsonSchemaRepository.getJsonSchema(null));
	}

}
