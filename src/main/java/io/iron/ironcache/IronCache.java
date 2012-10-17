/**
 * 
 */
package io.iron.ironcache;

import java.io.IOException;
import java.util.List;

/**
 * @author pjarrell
 * 
 */
public interface IronCache {

	void delete(final String key) throws IOException;

	void deleteItem(String cacheName, String key) throws IOException;

	CacheItem get(final String key) throws IOException;

	Cache getCache(final String cacheName) throws IOException;

	List<Cache> getCaches() throws IOException;

	List<Cache> getCaches(final int page) throws IOException;

	CacheItem getItem(final String cacheName, final String key) throws IOException;

	int increment(final String key, final int amount) throws IOException;

	int incrementItem(String cacheName, String key, int amount) throws IOException;

	void put(final String key, final String value) throws IOException;

	void put(final String key, final String value, boolean add, boolean replace) throws IOException;

	void putItem(final String cacheName, final String key, final String value) throws IOException;

	void putItem(final String cacheName, final String key, final String value, boolean add, boolean replace)
			throws IOException;

	void setCacheName(final String cacheName);

}
