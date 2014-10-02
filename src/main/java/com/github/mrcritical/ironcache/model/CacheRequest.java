/**
 * 
 */
package com.github.mrcritical.ironcache.model;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object that represents a request to be sent to IronCache.
 * 
 * @author pjarrell
 *
 */
@Data
public class CacheRequest implements Serializable {

	private static final long serialVersionUID = -8084279185401021443L;

	@JsonProperty("add")
	private boolean add;

	@JsonProperty("expires_in")
	private String expires;

	@JsonProperty("replace")
	private boolean replace;

	@JsonProperty("value")
	private String value;

	public CacheRequest add(boolean add) {
		this.add = add;
		return this;
	}

	public CacheRequest expires(String expires) {
		this.expires = expires;
		return this;
	}

	public CacheRequest replace(boolean replace) {
		this.replace = replace;
		return this;
	}

	public CacheRequest value(String value) {
		this.value = value;
		return this;
	}

}
