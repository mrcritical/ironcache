/**
 *
 */
package com.github.mrcritical.ironcache.internal.model;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;

/**
 * A cache response model.
 *
 * @author pjarrell
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CacheResponse implements Serializable {

	private static final long serialVersionUID = 4560045508958843229L;

	@JsonProperty("msg")
	@Key
	private String message;

}
