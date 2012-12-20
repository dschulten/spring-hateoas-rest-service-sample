package org.springframework.hateoas.client;

public class GoalNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GoalNotFoundException() {
		super();
	}

	public GoalNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoalNotFoundException(String message) {
		super(message);
	}

	public GoalNotFoundException(Throwable cause) {
		super(cause);
	}
	
	

}
