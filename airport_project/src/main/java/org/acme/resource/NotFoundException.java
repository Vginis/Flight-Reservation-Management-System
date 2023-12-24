package org.acme.resource;

import org.acme.AcmeException;

import java.io.Serial;

public class NotFoundException extends AcmeException {

	@Serial
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
