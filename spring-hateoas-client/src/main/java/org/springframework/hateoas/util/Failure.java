package org.springframework.hateoas.util;

public class Failure {

    public static RuntimeException asUnchecked(Exception e) {
	if (e instanceof RuntimeException) {
	    return (RuntimeException) e;
	} else {
	    return new RuntimeException(e);
	}
    }
}
