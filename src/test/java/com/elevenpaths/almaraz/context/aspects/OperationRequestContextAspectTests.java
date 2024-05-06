/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */
package com.elevenpaths.almaraz.context.aspects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SuppressWarnings("unchecked")
public class OperationRequestContextAspectTests {

	private OperationRequestContextAspect operationLogAspect;
	private OperationRequestContext operationRequestContext;
	private OperationRequestContext emptyOperationRequestContext;
	
	@BeforeEach
	public void init() {
		operationLogAspect = new OperationRequestContextAspect();
		operationRequestContext = new OperationRequestContext() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			
			@Override
			public String value() {
				return "operation";
			}
		};
		
		emptyOperationRequestContext = new OperationRequestContext() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}
			
			@Override
			public String value() {
				return "";
			}
		};
	}

	@Test
	public void addAnnotationOperationToRequestContextWithMonoMethodTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		Signature signature = mock(Signature.class);
		when(signature.getName()).thenReturn("methodName");
		when(joinPoint.proceed()).thenReturn(Mono.just(true));
		when(joinPoint.getSignature()).thenReturn(signature);
		
		StepVerifier.create((Mono<Boolean>) operationLogAspect.operationLog(joinPoint, operationRequestContext))
			.expectNext(true)
			.verifyComplete();
	}
	
	@Test
	public void addEmptyAnnotationOperationToRequestContextWithMonoMethodTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		Signature signature = mock(Signature.class);
		when(signature.getName()).thenReturn("methodName");
		when(joinPoint.proceed()).thenReturn(Mono.just(true));
		when(joinPoint.getSignature()).thenReturn(signature);
		
		StepVerifier.create((Mono<Boolean>) operationLogAspect.operationLog(joinPoint, emptyOperationRequestContext))
			.expectNext(true)
			.verifyComplete();
	}
	
	@Test
	public void addAnnotationOperationToRequestContextWithFluxMethodTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		Signature signature = mock(Signature.class);
		when(signature.getName()).thenReturn("methodName");
		when(joinPoint.proceed()).thenReturn(Flux.just(true));
		when(joinPoint.getSignature()).thenReturn(signature);
		
		StepVerifier.create((Flux<Boolean>) operationLogAspect.operationLog(joinPoint, operationRequestContext))
			.expectNext(true)
			.verifyComplete();
	}
	
	@Test
	public void addAnnotationOperationToRequestContextNoPublisherMethodTest() throws Throwable {
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
		Signature signature = mock(Signature.class);
		when(signature.getName()).thenReturn("methodName");
		when(joinPoint.proceed()).thenReturn(true);
		when(joinPoint.getSignature()).thenReturn(signature);
		assertTrue((Boolean) operationLogAspect.operationLog(joinPoint, operationRequestContext));
	}

}
