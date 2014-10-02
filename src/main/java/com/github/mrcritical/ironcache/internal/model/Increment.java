package com.github.mrcritical.ironcache.internal.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;

/**
 * Object that represents an increment request.
 *
 * @author pjarrell
 *
 */
@Data
public class Increment {

	@JsonProperty("value")
	@Key
	private int amount;

	/**
	 * Add the amount to increment (or decrement if negative).
	 *
	 * @param amount
	 *            is the positive or negative value to increment or decrement by
	 * @return this
	 */
	public Increment amount(final int amount) {
		this.amount = amount;
		return this;
	}

}