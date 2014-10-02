package com.github.mrcritical.ironcache.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mrcritical.ironcache.model.Cache;

/**
 * Mixin to add Jackson annotations to {@link Cache}.
 *
 * @author pjarrell
 *
 */
public abstract class CacheMixin {

	@JsonCreator
	CacheMixin(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
			@JsonProperty("project_id") final String projectId, @JsonProperty("size") final Long size) {

	}

	@JsonProperty("id")
	abstract String getId();

	@JsonProperty("name")
	abstract String getName();

	@JsonProperty("project_id")
	abstract String getProjectId();

	@JsonProperty("size")
	abstract Long getSize();

}