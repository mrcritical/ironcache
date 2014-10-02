/**
 *
 */
package com.github.mrcritical.ironcache;

import java.util.List;

import com.github.mrcritical.ironcache.model.Cache;
import com.github.mrcritical.ironcache.model.CacheItemRequest;
import com.github.mrcritical.ironcache.model.CachedItem;
import com.google.common.base.Optional;

/**
 * This is an Iron.io cache client that handles the interaction with the Iron.io
 * cache. A different cache instance is expected to be created for each project.
 *
 * @author pjarrell
 *
 */
public interface IronCacheProvider {

	/**
	 * Retrieve all the caches currently available.
	 *
	 * @return a list of caches, empty if none were found
	 */
	List<Cache> listCaches();

	/**
	 * Gets the cache with the name specified. This will return the current size
	 * of the cache (number of items).
	 *
	 * @param name
	 *            is the name of cache to retrieve
	 * @return an Optional with the cache, or empty if not found
	 */
	Optional<Cache> getCache(final String name);

	/**
	 * Gets the desired item from the cache.
	 *
	 * @param name
	 *            the name of the cache the item is stored in
	 * @param key
	 *            is the key to the item to retrieve
	 * @return an Optional with the item, or empty if not found
	 */
	Optional<CachedItem> getItem(final String name, final String key);

	/**
	 * Puts the item in the cache. All the conditions specified in the item
	 * request must be matched for the item to be stored. Please see
	 * {@link CacheItemRequest} for details.
	 *
	 * @param name
	 *            is the name of the cache to store into
	 * @param request
	 *            the request containing the item to store in the cache
	 */
	void putItem(final String name, final CacheItemRequest request);

	/**
	 * Increments (or decrements) the value of the item in the cache. This only
	 * works if the value was a number. It will fail otherwise.
	 *
	 * @param name
	 *            is the cache the item is stored in
	 * @param key
	 *            is the key to the item to increment
	 * @param amount
	 *            is the amount to increment (a negative value will decrement)
	 */
	void incrementItem(final String name, final String key, final int amount);

	/**
	 * Removes all the items stored in the specified cache. This is destructive
	 * and will remove all items in that cache.
	 *
	 * @param name
	 *            is the name of the cache to clear
	 */
	void clearCache(final String name);

	/**
	 * Removes all the items in the cache and removes the cache itself.
	 *
	 * @param name
	 *            is the name of the cache to remove, along with all it's items
	 */
	void deleteCache(final String name);

	/**
	 * Removes the item from the specified cache.
	 *
	 * @param name
	 *            is the cache this item is stored in
	 * @param key
	 *            is the key to the item to remove
	 */
	void deleteItem(final String name, final String key);

}
