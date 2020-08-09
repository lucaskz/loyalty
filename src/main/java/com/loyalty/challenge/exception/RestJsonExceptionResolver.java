package com.loyalty.challenge.exception;

import java.util.Date;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler, defines which and how to respond in front of different exceptions.
 *
 * @author Lucas Kaseta
 */

@ControllerAdvice
public class RestJsonExceptionResolver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestJsonExceptionResolver.class);
	
	@ExceptionHandler({InvalidArgumentException.class, IllegalArgumentException.class, MissingServletRequestParameterException.class, HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<?> handleBadRequestException(Exception ex, WebRequest request) {
		LOGGER.info(ex.getMessage(), ex);
		return createErrorResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ResponseEntity<?> handleMessageNotReadable(Exception ex, WebRequest request) {
		LOGGER.info(ex.getMessage(), ex);
		return createErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ValidationException.class, ExternalResourceException.class})
	public ResponseEntity<?> handleValidationException(Exception ex, WebRequest request) {
		LOGGER.info(ex.getCause().getMessage(), ex);
		return createErrorResponse(ex.getCause().getMessage(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({TimeoutException.class})
	public ResponseEntity<?> handleTimeoutException(Exception ex, WebRequest request) {
		LOGGER.info(ex.getCause().getMessage(), ex);
		return createErrorResponse(ex.getCause().getMessage(), request, HttpStatus.REQUEST_TIMEOUT);
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<?> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		return createErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), request, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(Exception ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		return createErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase(), request, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
		LOGGER.error(ex.getMessage(), ex);
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), request, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private static ResponseEntity<?> createErrorResponse(String message, WebRequest request, HttpStatus httpStatus) {
		ErrorDetail errorDetails = new ErrorDetail(new Date(), message, request.getDescription(false));
		return new ResponseEntity<>(errorDetails, httpStatus);
	}
}
