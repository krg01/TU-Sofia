package com.config.exception;

public class CreateUserException extends Exception {

	private String message;
	
	public CreateUserException(String string) {
		this.message = string;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

}
