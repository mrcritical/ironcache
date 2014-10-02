/**
 *
 */
package com.github.mrcritical.ironcache.internal.model;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;

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
	@Key
	private Boolean add;

	@JsonProperty("cas")
	@Key
	private String cas;

	@JsonProperty("expires_in")
	@Key
	private Long expires;

	@JsonProperty("replace")
	@Key
	private Boolean replace;

	@JsonProperty("value")
	@Key
	private Object value;

	public CacheRequest add(final boolean add) {
		this.add = add;
		return this;
	}

	public CacheRequest cas(final String cas) {
		this.cas = cas;
		return this;
	}

	public CacheRequest expires(final Long expires) {
		this.expires = expires;
		return this;
	}

	public CacheRequest replace(final boolean replace) {
		this.replace = replace;
		return this;
	}

	public CacheRequest value(final String value) {
		this.value = value;
		return this;
	}

	public CacheRequest value(final Object value) {
		this.value = value;
		return this;
	}

}
