/**
 *
 */
package com.github.mrcritical.ironcache.model;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This represents a request to be made to store in a item in the cache. It
 * exposes the ability to handle CAS, expiration, and replace/add only
 * functionality.
 *
 * @author pjarrell
 *
 */
@Getter(AccessLevel.PUBLIC)
@EqualsAndHashCode
public class CacheItemRequest implements Serializable {

	/**
	 * Creates a new cache item request.
	 *
	 * @return the cache item request
	 */
	public static CacheItemRequest create() {
		return new CacheItemRequest();
	}

	private static final long serialVersionUID = -5363002182169874301L;

	private String cas;

	private Long expireAfter;

	private String key;

	private Boolean onlyIfExists;

	private Boolean onlyIfNotExists;

	private Object value;

	/**
	 * Hide the default constructor.
	 */
	private CacheItemRequest() {
	}

	/**
	 * Sets a second value that should be matched before adding this item to the
	 * cache. This value should only be set when <strong>modifying</strong> an
	 * existing item in the cache. When using this the key and CAS must match
	 * the existing items key and CAS before the item is stored. The CAS value
	 * will be updated after the item is updated.
	 *
	 * @param cas
	 *            is the value to match against the existing item in the cache
	 * @return this
	 */
	public CacheItemRequest cas(final String cas) {
		this.cas = cas;
		return this;
	}

	/**
	 * Sets the expiration, in seconds, in which this item should automatically
	 * be removed from the cache.
	 *
	 * @param expireAfter
	 *            is the number of seconds that should pass before removing the
	 *            item
	 * @return this
	 */
	public CacheItemRequest expireAfter(final long expireAfter) {
		this.expireAfter = expireAfter;
		return this;
	}

	/**
	 * Sets the key that will identify this cached item.
	 *
	 * @param key
	 *            is the unique key for this item
	 * @return this
	 */
	public CacheItemRequest key(final String key) {
		this.key = key;
		return this;
	}

	/**
	 * Defines that this item should <strong>only</strong> be stored in the
	 * cache if an existing item with the same key <strong>already
	 * exists</strong>. Otherwise the item will not be stored in the cache.
	 *
	 * @return this
	 */
	public CacheItemRequest onlyIfExists() {
		this.onlyIfExists = true;
		return this;
	}

	/**
	 * Defines that this item should <strong>only</strong> be stored in the
	 * cache if an item with the same key <strong>does not already
	 * exist</strong> in the cache.
	 *
	 * @return this
	 */
	public CacheItemRequest onlyIfNotExists() {
		this.onlyIfNotExists = true;
		return this;
	}

	/**
	 * Sets the value to store in the cache.
	 *
	 * @param value
	 *            is the value to store
	 * @return this
	 */
	public CacheItemRequest value(final String value) {
		this.value = value;
		return this;
	}

	/**
	 * Sets the value to store in the cache.
	 *
	 * @param value
	 *            is the value to store
	 * @return this
	 */
	public CacheItemRequest value(Object value) {
		this.value = value;
		return this;
	}

}
