/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.elevenpaths.almaraz.context;

import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Mono;

/**
 * Basic context with relevant information about the request/response.
 * This information can be used to log messages with contextual information in JSON format.
 *
 * In order to add custom properties to the context, it is possible to use the internal {@link #contextMap}
 * or to extend the class (to provide get/set methods). In both cases, the {@link #contextMap} must store
 * all the context information.
 *
 * {@link RequestContext} is a fluent API.
 *
 * @author Jorge Lorenzo <jorge.lorenzogallardo@telefonica.com>
 *
 */
public class RequestContext {

	/**
	 * Field name for the transaction id.
	 */
	public static final String TRANSACTION_ID = "trans";

	/**
	 * Field name for the correlator.
	 */
	public static final String CORRELATOR = "corr";

	/**
	 * Field name for the operation.
	 */
	public static final String OPERATION = "op";

	/**
	 * Field name for the service.
	 */
	public static final String SERVICE = "svc";

	/**
	 * Field name for the component.
	 */
	public static final String COMPONENT = "comp";

	/**
	 * Field name for the user.
	 */
	public static final String USER = "user";

	/**
	 * Field name for the realm.
	 */
	public static final String REALM = "realm";

	/**
	 * Field name for the alarm.
	 */
	public static final String ALARM = "alarm";

	/**
	 * Context map with all the context properties.
	 * Property values must be {@link String} due to a limitation of MDC (Mapped Diagnostic Context).
	 */
	private Map<String, String> contextMap;

	/**
	 * Constructor.
	 */
	public RequestContext() {
		this.contextMap = new HashMap<>();
	}

	/**
	 * Set the transaction id in the {@link #contextMap}.
	 *
	 * @param transactionId
	 * @return
	 */
	public RequestContext setTransactionId(String transactionId) {
		return put(TRANSACTION_ID, transactionId);
	}

	/**
	 * Get the transaction id.
	 *
	 * @return
	 */
	public String getTransactionId() {
		return getString(TRANSACTION_ID);
	}

	/**
	 * Set the correlator in the {@link #contextMap}.
	 *
	 * @param correlator
	 * @return
	 */
	public RequestContext setCorrelator(String correlator) {
		return put(CORRELATOR, correlator);
	}

	/**
	 * Get the correlator.
	 *
	 * @return
	 */
	public String getCorrelator() {
		return getString(CORRELATOR);
	}

	/**
	 * Set the operation in the {@link #contextMap}.
	 *
	 * @param operation
	 * @return
	 */
	public RequestContext setOperation(String operation) {
		return put(OPERATION, operation);
	}

	/**
	 * Get the operation.
	 *
	 * @return
	 */
	public String getOperation() {
		return getString(OPERATION);
	}

	/**
	 * Set the service in the {@link #contextMap}.
	 *
	 * @param service
	 * @return
	 */
	public RequestContext setService(String service) {
		return put(SERVICE, service);
	}

	/**
	 * Get the service.
	 *
	 * @return
	 */
	public String getService() {
		return getString(SERVICE);
	}

	/**
	 * Set the component in the {@link #contextMap}.
	 *
	 * @param component
	 * @return
	 */
	public RequestContext setComponent(String component) {
		return put(COMPONENT, component);
	}

	/**
	 * Get the component.
	 *
	 * @return
	 */
	public String getComponent() {
		return getString(COMPONENT);
	}

	/**
	 * Set the user in the {@link #contextMap}.
	 *
	 * @param user
	 * @return
	 */
	public RequestContext setUser(String user) {
		return put(USER, user);
	}

	/**
	 * Get the user.
	 *
	 * @return
	 */
	public String getUser() {
		return getString(USER);
	}

	/**
	 * Set the realm in the {@link #contextMap}.
	 *
	 * @param realm
	 * @return
	 */
	public RequestContext setRealm(String realm) {
		return put(REALM, realm);
	}

	/**
	 * Get the realm.
	 *
	 * @return
	 */
	public String getRealm() {
		return getString(REALM);
	}

	/**
	 * Set the alarm in the {@link #contextMap}.
	 *
	 * @param alarm
	 * @return
	 */
	public RequestContext setAlarm(String alarm) {
		return put(ALARM, alarm);
	}

	/**
	 * Get the alarm.
	 *
	 * @return
	 */
	public String getAlarm() {
		return getString(ALARM);
	}

	/**
	 * Set a context property directly in the {@link #contextMap}.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public RequestContext put(String key, String value) {
		contextMap.put(key, value);
		return this;
	}

	/**
	 * Get a context property directly from the {@link #contextMap}.
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return contextMap.get(key);
	}

	/**
	 * Set a {@link Long} context property directly in the {@link #contextMap}.
	 * The value is stored in the map as a {@link String} due to MDC limitations.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public RequestContext put(String key, Long value) {
		String strValue = (value == null) ? null : Long.toString(value);
		return put(key, strValue);
	}

	/**
	 * Get a context property directly from the {@link #contextMap} converting it to {@link Long}.
	 *
	 * @param key
	 * @return
	 */
	public Long getLong(String key) {
		try {
			return Long.valueOf(getString(key));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Set a {@link Boolean} context property directly in the {@link #contextMap}.
	 * The value is stored in the map as a {@link String} due to MDC limitations.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public RequestContext put(String key, Boolean value) {
		String strValue = (value == null) ? null : Boolean.toString(value);
		return put(key, strValue);
	}

	/**
	 * Get a context property directly from the {@link #contextMap} converting it to {@link Boolean}.
	 *
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		try {
			return Boolean.valueOf(getString(key));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retrieve the whole {@link #contextMap}.
	 *
	 * @return
	 */
	public Map<String, String> getContextMap() {
		return this.contextMap;
	}


	/**
	 * Retrieve the context from the reactive stream.
	 *
	 * @return
	 */
	public static Mono<RequestContext> context() {
		return Mono.subscriberContext()
				.map(ctxt -> ctxt.getOrDefault(RequestContext.class, new RequestContext()));
	}

}
