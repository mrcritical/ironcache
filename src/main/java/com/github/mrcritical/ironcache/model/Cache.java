/**
 *
 */
package com.github.mrcritical.ironcache.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a particular cache in Iron.io. The property {@link #size} is only
 * available when a specific cache is requested. It is not available when
 * listing the available caches.
 *
 * @author pjarrell
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cache implements Serializable {

	private static final long serialVersionUID = 3073411981043080949L;

	private String id;

	private String name;

	@JsonProperty("project_id")
	private String projectId;

	private Long size;

}
