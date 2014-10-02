package com.github.mrcritical.ironcache.internal.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object that represents an error response.
 *
 * @author pjarrell
 *
 */
@Data
public class CacheError {

	@JsonProperty("msg")
	private String message;

}