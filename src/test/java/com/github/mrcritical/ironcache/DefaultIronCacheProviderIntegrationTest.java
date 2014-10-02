/**
 *
 */
package com.github.mrcritical.ironcache;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mrcritical.ironcache.model.Cache;
import com.github.mrcritical.ironcache.model.CacheItemRequest;
import com.github.mrcritical.ironcache.model.CachedItem;
import com.google.api.client.util.Strings;
import com.google.common.base.Optional;

/**
 * @author pjarrell
 *
 */
public class DefaultIronCacheProviderIntegrationTest {

	private static DefaultIronCacheProvider cacheProvider;

	private static final int EXPIRATION_IN_SECONDS = 60 * 5;

	private static final String TEST_CACHE_NAME_PREFIX = "Cache-Test-";

	private static final String TEST_CACHE_PREFIX_KEY = "key-";

	private static final String TEST_CACHE_PREFIX_NAME = "Test Item ";

	private String cacheName;

	private int index;

	private void addToCache(final String cacheName, final int index, Object value) {
		final CacheItemRequest request = CacheItemRequest.create().key(TEST_CACHE_PREFIX_KEY + index).value(value)
				.expireAfter(EXPIRATION_IN_SECONDS).onlyIfNotExists();
		cacheProvider.putItem(cacheName, request);
	}

	private void primeTheCache(final String cacheName) {
		for (int i = 0; i < 10; i++) {
			addToCache(cacheName, i, TEST_CACHE_PREFIX_NAME + index);
		}
	}

	@Before
	public void setUp() {
		cacheProvider = new DefaultIronCacheProvider(System.getProperty("token"), System.getProperty("project_id"));
		cacheName = TEST_CACHE_NAME_PREFIX + UUID.randomUUID().toString();
		index++;
	}

	@After
	public void tearDownAfter() {
		try {
			cacheProvider.deleteCache(cacheName);
		} catch (Exception e) {
			// Ignore since we are just trying to cleanup a bit
		}
	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#clearCache(java.lang.String)}
	 * .
	 *
	 * @throws URISyntaxException
	 */
	@Test
	public void testClearCache() throws URISyntaxException {

		// Add items to the cache first
		primeTheCache(cacheName);

		Assert.assertTrue(cacheProvider.getCache(cacheName).or(new Cache()).getSize() > 0);

		cacheProvider.clearCache(cacheName);

		Assert.assertTrue(cacheProvider.getCache(cacheName).get().getSize() == 0);
	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#deleteCache(java.lang.String)}
	 * .
	 */
	@Test
	public void testDeleteCache() {

		addToCache(cacheName, index, TEST_CACHE_PREFIX_NAME + index);

		cacheProvider.deleteCache(cacheName);

		Assert.assertFalse(cacheProvider.getCache(cacheName).isPresent());

	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#deleteItem(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testDeleteItem() {

		addToCache(cacheName, index, TEST_CACHE_PREFIX_NAME + index);

		Assert.assertTrue(cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index).isPresent());

		cacheProvider.deleteItem(cacheName, TEST_CACHE_PREFIX_KEY + index);

		Assert.assertFalse(cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index).isPresent());

	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#getCache(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetCache() {

		addToCache(cacheName, index, TEST_CACHE_PREFIX_NAME + index);

		Assert.assertTrue(cacheProvider.getCache(cacheName).isPresent());

	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#getItem(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetItem() {

		addToCache(cacheName, index, TEST_CACHE_PREFIX_NAME + index);

		final Optional<CachedItem> item = cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index);
		Assert.assertTrue(item.isPresent());
		Assert.assertEquals(TEST_CACHE_PREFIX_KEY + index, item.get().getKey());
		Assert.assertEquals(TEST_CACHE_PREFIX_NAME + index, item.get().getValue());
		Assert.assertFalse(Strings.isNullOrEmpty(item.get().getCas()));
		Assert.assertNotNull(item.get().getExpires());

	}

	@Test
	public void testGetItem_UTF8() {
		String hebrew = "אני אוהב שוקולד";

		addToCache(cacheName, index, hebrew);

		final Optional<CachedItem> item = cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index);
		Assert.assertTrue(item.isPresent());
		Assert.assertEquals(TEST_CACHE_PREFIX_KEY + index, item.get().getKey());
		Assert.assertEquals(hebrew, item.get().getValue());
		Assert.assertFalse(Strings.isNullOrEmpty(item.get().getCas()));
		Assert.assertNotNull(item.get().getExpires());

	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#incrementItem(java.lang.String, java.lang.String, int)}
	 * .
	 */
	@Test
	public void testIncrementItem() {

		cacheProvider.putItem(cacheName, CacheItemRequest.create().key(TEST_CACHE_PREFIX_KEY + index).value(1)
				.expireAfter(EXPIRATION_IN_SECONDS).onlyIfNotExists());

		cacheProvider.incrementItem(cacheName, TEST_CACHE_PREFIX_KEY + index, 1);

		final Optional<CachedItem> item = cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index);
		Assert.assertEquals(2, item.get().getValue());
	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#listCaches()}
	 * .
	 */
	@Test
	public void testListCaches() {

		addToCache(cacheName, index, TEST_CACHE_PREFIX_NAME + index);

		final List<Cache> caches = cacheProvider.listCaches();
		Assert.assertTrue(caches.size() > 0);

		Assert.assertNotNull(caches.get(0).getName());
		Assert.assertNotNull(caches.get(0).getId());
		Assert.assertNotNull(caches.get(0).getProjectId());
		Assert.assertNull(caches.get(0).getSize());

	}

	/**
	 * Test method for
	 * {@link com.github.mrcritical.ironcache.DefaultIronCacheProvider#putItem(java.lang.String, com.github.mrcritical.ironcache.model.CacheItemRequest)}
	 * .
	 */
	@Test
	public void testPutItem() {

		cacheProvider.putItem(cacheName, CacheItemRequest.create().key(TEST_CACHE_PREFIX_KEY + index).value("1")
				.expireAfter(EXPIRATION_IN_SECONDS).onlyIfNotExists());

		Assert.assertTrue(cacheProvider.getItem(cacheName, TEST_CACHE_PREFIX_KEY + index).isPresent());

	}

}
