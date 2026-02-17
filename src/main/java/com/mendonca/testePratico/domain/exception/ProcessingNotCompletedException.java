package com.mendonca.testePratico.domain.exception;

public class ProcessingNotCompletedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProcessingNotCompletedException(String message) {
        super(message);
    }
}
