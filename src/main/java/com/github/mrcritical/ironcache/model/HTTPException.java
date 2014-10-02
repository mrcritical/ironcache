package com.github.mrcritical.ironcache.model;

import java.io.IOException;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author pjarrell
 *
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class HTTPException extends IOException {

	private static final long serialVersionUID = -5125419459309848324L;

	private final int statusCode;

	/**
	 * Creates a new HTTPException.
	 *
	 * @param statusCode
	 *            The HTTP status code.
	 * @param message
	 *            The text of the HTTP status code.
	 */
	public HTTPException(final int statusCode, final String message) {
		super(message);
		this.statusCode = statusCode;
	}

}
