/**
 * 
 */
package com.github.mrcritical.ironcache.internal.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;

/**
 * The response on an increment.
 * 
 * @author pjarrell
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncrementCacheResponse extends CacheResponse {

	private static final long serialVersionUID = -1129071087436832447L;

	@JsonProperty("value")
	@Key
	private Integer value;

}
