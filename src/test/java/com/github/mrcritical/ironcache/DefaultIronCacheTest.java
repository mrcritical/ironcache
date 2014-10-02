package com.github.mrcritical.ironcache;

import static org.junit.Assert.fail;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

/**
 * @author pjarrell
 *
 */
public class DefaultIronCacheTest {

	static String CACHE_ITEM_1_RESPONSE;

	static String CACHE_ITEM_2_RESPONSE;

	static String CACHES_1_RESPONSE;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CACHES_1_RESPONSE = IOUtils.toString(Resources.getResource(
				"caches_1.json").openStream());
		CACHE_ITEM_1_RESPONSE = IOUtils.toString(Resources.getResource(
				"cache_item_1.json").openStream());
		CACHE_ITEM_2_RESPONSE = IOUtils.toString(Resources.getResource(
				"cache_item_2.json").openStream());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_BadApiVersion_TooSmall() {
		DefaultIronCache.configure("https", "localhost", 8080, -100,
				"30760ccba2fa930e1e006343");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_BadApiVersion_Zero() {
		DefaultIronCache.configure("https", "localhost", 8080, 0,
				"30760ccba2fa930e1e006343");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_BadPort_TooSmall() {
		DefaultIronCache.configure("https", "localhost", -100, 2,
				"30760ccba2fa930e1e006343");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_BadPort_Zerol() {
		DefaultIronCache.configure("https", "localhost", 0, 2,
				"30760ccba2fa930e1e006343");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_MissingHost() {
		DefaultIronCache.configure("https", null, 8080, 2,
				"30760ccba2fa930e1e006343");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_MissingProjectID() {
		DefaultIronCache.configure("https", "localhost", 8080, 2, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConfigureAPIURL_MissingProtocol() {
		DefaultIronCache.configure(null, "localhost", 8080, 2,
				"30760ccba2fa930e1e006343");
	}

	@Test
	public void testConfigureAPIURL_Success() {
		String apiURL = DefaultIronCache.configure("https", "localhost", 8080,
				2, "30760ccba2fa930e1e006343");
		Assert.assertEquals(
				"https://localhost:8080/2/projects/30760ccba2fa930e1e006343/",
				apiURL);
	}

	@Test
	public void testConfigureObjectMapper_NotRegistered() throws Exception {
		ObjectMapper json = new ObjectMapper();
		try {
			json.readValue(CACHE_ITEM_1_RESPONSE, CacheItem.class);
		} catch (Exception e) {
			Assert.assertEquals(JsonMappingException.class, e.getClass());
		}
	}

	@Test
	public void testConfigureObjectMapper_Registered() throws Exception {
		ObjectMapper json = new ObjectMapper();
		json = DefaultIronCache.configure(json);
		CacheItem item = json.readValue(CACHE_ITEM_1_RESPONSE, CacheItem.class);
		Assert.assertEquals("foo@bar.org", item.getKey());
	}

	@Test
	@Ignore
	public void testDefaultIronCacheString() {
		fail("Not yet implemented");
	}

	@Test
	public void testDefaultIronCacheStringString() {
		new DefaultIronCache("test-token", "test-project");
	}

	@Test
	@Ignore
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteItem() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGet() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetCache() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetCaches() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetCachesInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetItem() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIncrement() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIncrementItem() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPutItemStringStringString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPutItemStringStringStringBooleanBoolean() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPutStringString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPutStringStringBooleanBoolean() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRequest() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetCacheName() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSingleRequest() {
		fail("Not yet implemented");
	}

}
