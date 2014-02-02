package com.github.mrcritical.ironcache;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DefaultIronCache implements IronCache {

	/**
	 * Mixin to add Jackson annotations to {@link CacheItem}.
	 * 
	 * @author pjarrell
	 * 
	 */
	static abstract class CacheItemMixin {

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
		@JsonSerialize(using = JodaDateTimeSerializer.class)
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

	/**
	 * Mixin to add Jackson annotations to {@link Cache}.
	 * 
	 * @author pjarrell
	 * 
	 */
	static abstract class CacheMixin {

		@JsonCreator
		CacheMixin(@JsonProperty("id") final String id, @JsonProperty("name") final String name,
				@JsonProperty("project_id") final String projectId) {

		}

		@JsonProperty("id")
		abstract String getId();

		@JsonProperty("name")
		abstract String getName();

		@JsonProperty("project_id")
		abstract String getProjectId();

	}

	/**
	 * Object that represents an error response.
	 * 
	 * @author pjarrell
	 * 
	 */
	static private class Error {

		@JsonProperty("msg")
		String msg;

	}

	/**
	 * Object that represents an increment request.
	 * 
	 * @author pjarrell
	 * 
	 */
	static private class Increment {

		@JsonProperty("value")
		int amount;

		@JsonProperty("msg")
		String msg;

	}

	/**
	 * Deserialize a Joda {@link LocalDateTime}.
	 * 
	 * @author pjarrell
	 * 
	 */
	static class JodaDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

		private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(
				DateTimeZone.UTC);

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus
		 * .jackson .JsonParser,
		 * org.codehaus.jackson.map.DeserializationContext)
		 */
		@Override
		public LocalDateTime deserialize(final JsonParser parser, final DeserializationContext context)
				throws IOException, JsonProcessingException {
			return formatter.parseLocalDateTime(parser.getText());
		}

	}

	/**
	 * Serialize a Joda {@link LocalDateTime}.
	 * 
	 * @author pjarrell
	 * 
	 */
	static class JodaDateTimeSerializer extends JsonSerializer<LocalDateTime> {

		private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(
				DateTimeZone.UTC);

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
		 * org.codehaus.jackson.JsonGenerator,
		 * org.codehaus.jackson.map.SerializerProvider)
		 */
		@Override
		public void serialize(final LocalDateTime value, final JsonGenerator jgen, final SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeString(value.toString(formatter));
		}

	}

	/**
	 * Object that represents a request to be sent to IronCache.
	 * 
	 * @author pjarrell
	 * 
	 */
	private class Request {

		@JsonProperty("add")
		boolean add;

		@JsonProperty("expires_in")
		String expires;

		@JsonProperty("replace")
		boolean replace;

		@JsonProperty("value")
		String value;

	}

	private static final String API_VERSION_KEY = "api_version";

	private static final String CACHE_NAME_KEY = "cache_name";

	private static final String HOST_KEY = "host";

	private static final Log logger = LogFactory.getLog(DefaultIronCache.class);

	private static final String PORT_KEY = "port";

	private static final String PROJECT_ID_KEY = "project_id";

	private static final String PROTOCOL_KEY = "protocol";

	static final Random rand = new Random();

	private static final String TOKEN_KEY = "token";

	/**
	 * Add mixins to the {@link ObjectMapper} so Jackson can
	 * serialize/deserialize cache objects.
	 * 
	 * @param json
	 *            is the {@link ObjectMapper} to augment
	 */
	protected static ObjectMapper configure(final ObjectMapper json) {
		json.registerModule(new SimpleModule("CacheMixins", new Version(1, 0, 0, null)) {
			@Override
			public void setupModule(final SetupContext context) {
				context.setMixInAnnotations(CacheItem.class, CacheItemMixin.class);
				context.setMixInAnnotations(Cache.class, CacheMixin.class);
			}
		});
		return json;
	}

	/**
	 * Create the API URI from the passed information.
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 * @param apiVersion
	 * @param projectId
	 */
	protected static String configure(final String protocol, final String host, final int port, final int apiVersion,
			final String projectId) {
		checkArgument(protocol != null);
		checkArgument(host != null);
		checkArgument(port > 0);
		checkArgument(apiVersion > 0);
		checkArgument(projectId != null);
		return protocol + "://" + host + ":" + port + "/" + apiVersion + "/projects/" + projectId + "/";
	}

	private String apiURL;

	private int apiVersion = 1;

	private String cacheName;

	private final String clientName = "iron_cache_java";

	private final String clientVersion = "0.0.1";

	private String host = "cache-aws-us-east-1.iron.io";

	private HttpClient httpClient;

	private final ObjectMapper json;

	private int port = 443;

	private final String productName = "iron_cache";

	private String projectId;

	private String protocol = "https";

	private String token;

	/**
	 * Hidden constructor for configuring the default {@link ObjectMapper} and
	 * {@link HttpClient}. These can be overridden later using
	 * {@link #setJson(ObjectMapper)} and {@link #setHttpClient(HttpClient)}.
	 */
	private DefaultIronCache() {
		json = configure(new ObjectMapper());
		httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
	}

	/**
	 * Configure this cache using the configuration file (as available in the
	 * classpath).
	 * 
	 * @param configurationFile
	 */
	public DefaultIronCache(final String configurationFile) {
		this();

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configurationFile);
		if (is == null) {
			logger.warn("Failed to locate the configuration file [" + configurationFile + "], using defaults instead");
			return;
		}
		Properties properties = new Properties();
		try {
			properties.load(is);
			token = properties.getProperty(TOKEN_KEY, token);
			projectId = properties.getProperty(PROJECT_ID_KEY, projectId);
			port = Integer.valueOf(properties.getProperty(PORT_KEY, Integer.toString(port)));
			host = properties.getProperty(HOST_KEY, host);
			cacheName = properties.getProperty(CACHE_NAME_KEY);
			protocol = properties.getProperty(PROTOCOL_KEY, protocol);
			apiVersion = Integer.valueOf(properties.getProperty(API_VERSION_KEY, Integer.toString(apiVersion)));

		} catch (IOException e) {
			logger.error("Failed to read properties, using defaults", e);
		}
		apiURL = configure(protocol, host, port, apiVersion, projectId);
	}

	/**
	 * Configure this cache using the supplied token and project id.
	 * 
	 * @param token
	 *            is your authorization token provided by Iron.io
	 * @param projectId
	 *            sets the project that backs this cache
	 */
	public DefaultIronCache(final String token, final String projectId) {
		this();
		this.token = token;
		this.projectId = projectId;
		apiURL = configure(protocol, host, port, apiVersion, projectId);
	}

	/**
	 * Configure this cache using the supplied token and project id.
	 * 
	 * @param token
	 *            is your authorization token provided by Iron.io
	 * @param projectId
	 *            sets the project that backs this cache
	 * @param cacheName
	 *            sets the cache to be used by all calls that do not specify a
	 *            specify cache
	 */
	public DefaultIronCache(final String token, final String projectId, final String cacheName) {
		this(token, projectId);
		setCacheName(cacheName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#delete(java.lang.String)
	 */
	@Override
	public void delete(final String key) throws IOException {
		checkArgument(cacheName != null, "No cache set.  Must call #setCacheName(String) first.");
		HttpDelete request = new HttpDelete(apiURL + "caches/" + cacheName + "/items/" + key);
		request(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#deleteItem(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void deleteItem(final String cacheName, final String key) throws IOException {
		HttpDelete request = new HttpDelete(apiURL + "caches/" + cacheName + "/items/" + key);
		request(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#get(java.lang.String)
	 */
	@Override
	public CacheItem get(final String key) throws IOException {
		checkArgument(cacheName != null, "No cache set.  Must call #setCacheName(String) first.");
		return getItem(cacheName, key);
	}

	/**
	 * @return the apiURL
	 */
	protected String getApiURL() {
		return apiURL;
	}

	/**
	 * @return the apiVersion
	 */
	protected int getApiVersion() {
		return apiVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#getCache(java.lang.String)
	 */
	@Override
	public Cache getCache(final String cacheName) throws IOException {
		List<Cache> caches = getCaches();
		for (Cache cache : caches) {
			if (cache.getName().equals(cacheName)) {
				return cache;
			}
		}
		return null;
	}

	/**
	 * @return the cacheName
	 */
	protected String getCacheName() {
		return cacheName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#getCaches()
	 */
	@Override
	public List<Cache> getCaches() throws IOException {
		HttpGet request = new HttpGet(apiURL + "caches");
		return json.readValue(request(request), new TypeReference<List<Cache>>() {
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#getCaches(int)
	 */
	@Override
	public List<Cache> getCaches(final int page) throws IOException {
		HttpGet request = new HttpGet(apiURL + "caches?page=" + page);
		return json.readValue(request(request), new TypeReference<List<Cache>>() {
		});
	}

	/**
	 * @return the clientName
	 */
	protected String getClientName() {
		return clientName;
	}

	/**
	 * @return the clientVersion
	 */
	protected String getClientVersion() {
		return clientVersion;
	}

	/**
	 * @return the host
	 */
	protected String getHost() {
		return host;
	}

	/**
	 * @return the httpClient
	 */
	protected HttpClient getHttpClient() {
		return httpClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#getItem(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CacheItem getItem(final String cacheName, final String key) throws IOException {
		HttpGet request = new HttpGet(apiURL + "caches/" + cacheName + "/items/" + key);
		return json.readValue(request(request), CacheItem.class);
	}

	/**
	 * @return the json
	 */
	protected ObjectMapper getJson() {
		return json;
	}

	/**
	 * @return the port
	 */
	protected int getPort() {
		return port;
	}

	/**
	 * @return the productName
	 */
	protected String getProductName() {
		return productName;
	}

	/**
	 * @return the projectId
	 */
	protected String getProjectId() {
		return projectId;
	}

	/**
	 * @return the protocol
	 */
	protected String getProtocol() {
		return protocol;
	}

	/**
	 * @return the token
	 */
	protected String getToken() {
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#increment(java.lang.String, int)
	 */
	@Override
	public int increment(final String key, final int amount) throws IOException {
		checkArgument(cacheName != null, "No cache set.  Must call #setCacheName(String) first.");
		return incrementItem(cacheName, key, amount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#incrementItem(java.lang.String,
	 * java.lang.String, int)
	 */
	@Override
	public int incrementItem(final String cacheName, final String key, final int amount) throws IOException {
		HttpPost request = new HttpPost(apiURL + "caches/" + cacheName + "/items/" + key + "/increment?amount="
				+ amount);
		return json.readValue(request(request), Increment.class).amount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#put(java.lang.String, java.lang.String)
	 */
	@Override
	public void put(final String key, final String value) throws IOException {
		checkArgument(cacheName != null, "No cache set.  Must call #setCacheName(String) first.");
		putItem(cacheName, key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#put(java.lang.String, java.lang.String,
	 * boolean, boolean)
	 */
	@Override
	public void put(final String key, final String value, final boolean add, final boolean replace) throws IOException {
		checkArgument(cacheName != null, "No cache set.  Must call #setCacheName(String) first.");
		putItem(cacheName, key, value, add, replace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#putItem(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void putItem(final String cacheName, final String key, final String value) throws IOException {
		putItem(cacheName, key, value, false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#putItem(java.lang.String,
	 * java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public void putItem(final String cacheName, final String key, final String value, final boolean add,
			final boolean replace) throws IOException {
		HttpPut request = new HttpPut(apiURL + "caches/" + cacheName + "/items/" + key);
		Request req = new Request();
		req.value = value;
		req.add = add;
		req.replace = replace;
		StringEntity entity = new StringEntity(json.writeValueAsString(req));
		request.setEntity(entity);
		request(request);
	}

	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	protected Reader request(final HttpUriRequest request) throws IOException {
		final int maxRetries = 5;
		int retries = 0;
		while (true) {
			try {
				return singleRequest(request);
			} catch (HTTPException e) {
				// ELB sometimes returns this when load is increasing.
				// We retry with exponential backoff.
				if (e.getStatusCode() != 503 || retries >= maxRetries) {
					throw e;
				}
				retries++;
				// random delay between 0 and 4^tries*100 milliseconds
				int pow = (1 << (2 * retries)) * 100;
				int delay = rand.nextInt(pow);
				try {
					Thread.sleep(delay);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * @param apiVersion
	 *            the apiVersion to set
	 */
	public void setApiVersion(final int apiVersion) {
		this.apiVersion = apiVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.iron.ironcache.IronCache#setCacheName(java.lang.String)
	 */
	@Override
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @param httpClient
	 *            the httpClient to set
	 */
	public void setHttpClient(final HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * @param json
	 *            the json to set
	 */
	public void setJson(final ObjectMapper json) {
		configure(json);
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(final int port) {
		this.port = port;
	}

	/**
	 * @param projectId
	 *            the projectId to set
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(final String token) {
		this.token = token;
	}

	/**
	 * @param request
	 * @return
	 * @throws IOException
	 */
	protected Reader singleRequest(final HttpUriRequest request) throws IOException {
		request.setHeader("Authorization", "OAuth " + token);
		request.setHeader("User-Agent", "IronIO Java Client");
		if (!HttpDelete.class.isAssignableFrom(request.getClass())) {
			request.setHeader("Content-Type", "application/json");
		}
		int statusCode = 200;
		HttpEntity entity = null;
		long start = 0;
		long now = 0;
		try {
			start = System.currentTimeMillis();
			HttpResponse response = httpClient.execute(request);
			now = System.currentTimeMillis();
			logger.trace("Request to [" + request.getURI() + "] took " + (now - start) + "ms");
			statusCode = response.getStatusLine().getStatusCode();
			entity = response.getEntity();
		} catch (ClientProtocolException e) {
			throw new IOException(e);
		}
		if (statusCode != 200) {
			String msg;
			if (entity != null) {
				if (entity.getContentLength() > 0
						&& entity.getContentType().getValue().equalsIgnoreCase("application/json")) {
					InputStreamReader reader = new InputStreamReader(entity.getContent());
					try {
						msg = json.readValue(reader, Error.class).msg;
					} catch (JsonParseException e) {
						msg = "IronCache's response contained invalid JSON";
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
					throw new HTTPException(statusCode, msg);
				} else {
					msg = "Empty or non-JSON response";
				}
			}
		} else {
			return new InputStreamReader(entity.getContent());
		}
		return null;
	}

}
