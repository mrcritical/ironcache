package com.github.mrcritical.ironcache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

/**
 * Represents an item stored in the cache.
 *
 * @author pjarrell
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedItem {

	private String cache;

	private String cas;

	private DateTime expires;

	private int flags;

	private String key;

	private Object value;

}
