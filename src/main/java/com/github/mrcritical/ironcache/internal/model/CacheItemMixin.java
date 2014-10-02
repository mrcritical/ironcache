package com.github.mrcritical.ironcache.internal.model;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.mrcritical.ironcache.internal.serializers.JodaLocalDateTimeSerializer;
import com.github.mrcritical.ironcache.model.CachedItem;

/**
 * Mixin to add Jackson annotations to {@link CachedItem}.
 *
 * @author pjarrell
 *
 */
public abstract class CacheItemMixin {

	@JsonCreator
	CacheItemMixin(@JsonProperty("cache") final String cache, @JsonProperty("cas") final long cas,
			@JsonProperty("expires") final String expires, @JsonProperty("flags") final int flags,
			@JsonProperty("key") final String key, @JsonProperty("token") final String token,
			@JsonProperty("value") final String value) {

	}

	@JsonProperty("cache")
	abstract String getCache();

	@JsonProperty("cas")
	abstract long getCas();

	@JsonProperty("expires")
	@JsonSerialize(using = JodaLocalDateTimeSerializer.class)
	abstract LocalDateTime getExpires();

	@JsonProperty("flags")
	abstract int getFlags();

	@JsonProperty("key")
	abstract String getKey();

	@JsonProperty("token")
	abstract String getToken();

	@JsonProperty("value")
	abstract String getValue();

}