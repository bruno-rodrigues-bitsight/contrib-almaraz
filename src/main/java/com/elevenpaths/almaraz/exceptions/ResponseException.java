/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base exception with support to generate a JSON response with the error message.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "stackTrace", "status", "headers", "message", "localizedMessage", "suppressed" })
public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = 867007767497940895L;

	/**
	 * HTTP status to generate the error response.
	 */
	private final HttpStatus status;

	/**
	 * Error identifier.
	 */
	private final String error;

	/**
	 * Error description.
	 */
	private final String reason;

	/**
	 * Headers for the error response.
	 */
	private final Map<String, String> headers;

	/**
	 * Constructor with status.
	 *
	 * @param status
	 */
	public ResponseException(HttpStatus status) {
		this(status, null, null, null);
	}

	/**
	 * Constructor without exception.
	 *
	 * @param status
	 * @param error
	 * @param reason
	 */
	public ResponseException(HttpStatus status, @Nullable String error, @Nullable String reason) {
		this(status, error, reason, null);
	}

	/**
	 * Constructor with exception.
	 *
	 * @param status
	 * @param error
	 * @param reason
	 * @param t
	 */
	public ResponseException(HttpStatus status, @Nullable String error, @Nullable String reason, @Nullable Throwable t) {
		super(reason, t);
		this.status = status;
		this.error = error;
		this.reason = reason;
		this.headers = new HashMap<>();
	}

	/**
	 * Add an HTTP header for the error response.
	 *
	 * @param headerName
	 * @param headerValue
	 * @return
	 */
	public ResponseException addHeader(String headerName, String headerValue) {
		headers.put(headerName, headerValue);
		return this;
	}

	/**
	 * Get the HTTP status of the error response.
	 *
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}

	/**
	 * Get the error code of the error response.
	 * The error code is an identifier that categorizes the error.
	 *
	 * @return the error
	 */
	@JsonProperty("error")
	public String getError() {
		return error;
	}

	/**
	 * Get the error description of the error response.
	 *
	 * @return the reason
	 */
	@JsonProperty("error_description")
	public String getReason() {
		return reason;
	}

	/**
	 * Get the map of headers to be added to the error response.
	 *
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

}
