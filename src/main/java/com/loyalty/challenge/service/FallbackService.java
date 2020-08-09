package com.loyalty.challenge.service;

public interface FallbackService<T, S> {
	
	S fallback(T e, Throwable t);
}
