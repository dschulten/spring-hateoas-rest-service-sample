package de.escalon.rest.bt.dragon;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NO_CONTENT )
public class BehaviorFailedException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BehaviorFailedException() {
		super();
	}

	public BehaviorFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public BehaviorFailedException(String message) {
		super(message);
	}

	public BehaviorFailedException(Throwable cause) {
		super(cause);
	}



}
