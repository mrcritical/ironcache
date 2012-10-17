package io.iron.ironcache;

import java.io.IOException;

public class HTTPException extends IOException {

	private static final long serialVersionUID = -5125419459309848324L;

	private final int status;

	/**
	 * Creates a new HTTPException.
	 * 
	 * @param status
	 *            The HTTP status code.
	 * @param message
	 *            The text of the HTTP status code.
	 */
	public HTTPException(final int status, final String message) {
		super(message);
		this.status = status;
	}

	/**
	 * Returns the HTTP response's status code.
	 */
	public int getStatusCode() {
		return status;
	}

}
