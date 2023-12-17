package org.acme.resource;

import org.acme.AcmeException;

public class NotFoundException extends AcmeException {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}
}
