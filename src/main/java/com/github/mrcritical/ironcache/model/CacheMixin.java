package com.github.mrcritical.ironcache.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mrcritical.ironcache.Cache;

/**
 * Mixin to add Jackson annotations to {@link Cache}.
 * 
 * @author pjarrell
 * 
 */
@Data
public abstract class CacheMixin {

	@JsonCreator
	CacheMixin(@JsonProperty("id") final String id,
			@JsonProperty("name") final String name,
			@JsonProperty("project_id") final String projectId) {

	}

	@JsonProperty("id")
	abstract String getId();

	@JsonProperty("name")
	abstract String getName();

	@JsonProperty("project_id")
	abstract String getProjectId();

}