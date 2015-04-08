/**
 *
 */
package com.github.mrcritical.ironcache;

import java.util.List;
import java.util.Locale;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.annotation.ThreadSafe;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.mrcritical.ironcache.internal.http.ApacheHttpTransport;
import com.github.mrcritical.ironcache.internal.http.ResponseInitializer;
import com.github.mrcritical.ironcache.internal.model.CacheRequest;
import com.github.mrcritical.ironcache.internal.model.CacheResponse;
import com.github.mrcritical.ironcache.internal.model.Increment;
import com.github.mrcritical.ironcache.internal.model.IncrementCacheResponse;
import com.github.mrcritical.ironcache.internal.model.requests.CacheItemUrl;
import com.github.mrcritical.ironcache.internal.model.requests.CacheUrl;
import com.github.mrcritical.ironcache.internal.model.requests.CachesUrl;
import com.github.mrcritical.ironcache.internal.model.requests.ClearCacheUrl;
import com.github.mrcritical.ironcache.internal.model.requests.IncrementCacheItemUrl;
import com.github.mrcritical.ironcache.internal.serializers.JodaDateTimeDeserializer;
import com.github.mrcritical.ironcache.internal.serializers.JodaDateTimeSerializer;
import com.github.mrcritical.ironcache.model.Cache;
import com.github.mrcritical.ironcache.model.CacheItemRequest;
import com.github.mrcritical.ironcache.model.CachedItem;
import com.github.mrcritical.ironcache.model.HTTPException;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Strings;
import com.google.api.client.util.Throwables;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * A implementation of the cache client that is to handle each project
 * separately. The default hostname is "cache-aws-us-east-1". Unless you need to
 * explicitly set a different hostname you can use the
 * {@link #DefaultSimpleIronCache(String, String)} constructor.
 *
 * @author pjarrell
 *
 */
@Slf4j
@ThreadSafe
public class DefaultIronCacheProvider implements IronCacheProvider {

	/**
	 * Configures the ObjectMapper. Currently adds {@link AfterburnerModule} for
	 * more speed and Joda date/time serializer/deserializers.
	 *
	 * @param json
	 *            is the {@link ObjectMapper} to augment
	 */
	protected static ObjectMapper configure(final ObjectMapper json) {
		final SimpleModule jodaModule = new SimpleModule("Joda");
		jodaModule.addSerializer(new JodaDateTimeSerializer());
		jodaModule.addDeserializer(DateTime.class, new JodaDateTimeDeserializer());
		json.registerModule(jodaModule);

		// Increase performance even more
		json.registerModule(new AfterburnerModule());
		return json;
	}

	private static final String DEFAULT_HOST_NAME = "cache-aws-us-east-1";

	private static HttpTransport httpTransport;

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static ObjectMapper objectMapper;

	@Getter
	private final String hostName;

	@Getter
	private final String projectId;

	private final HttpRequestFactory requestFactory;

	@Getter(AccessLevel.PROTECTED)
	private final String token;

	/**
	 * Create a new cache for the specified project using the default hostname
	 * {@link #DEFAULT_HOST_NAME}.
	 *
	 * @param token
	 *            is the authentication token to use
	 * @param projectId
	 *            is the project id this cache is configured for
	 */
	public DefaultIronCacheProvider(final String token, final String projectId) {
		this(token, projectId, DEFAULT_HOST_NAME);
	}

	/**
	 * Create a new cache for the specified project.
	 *
	 * @param token
	 *            is the authentication token to use
	 * @param projectId
	 *            is the project id this cache is configured for
	 * @param hostName
	 *            is the hostname for the cache server
	 *            (https://<hostName>.iron.io, only supply the <hostName> value
	 *            in this argument)
	 */
	public DefaultIronCacheProvider(final String token, final String projectId, final String hostName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(projectId), "A valid project id is required");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(token), "A valid token is required");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(hostName), "A valid hostName is required");

		this.token = token;
		this.projectId = projectId;
		this.hostName = hostName;

		if (null == httpTransport) {
			DefaultIronCacheProvider.httpTransport = new ApacheHttpTransport();
		}

		if (null == objectMapper) {
			DefaultIronCacheProvider.objectMapper = configure(new ObjectMapper());
		}

		requestFactory = DefaultIronCacheProvider.httpTransport.createRequestFactory(new ResponseInitializer(token,
				JSON_FACTORY));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#clearCache(java.lang.
	 * String)
	 */
	@Override
	public void clearCache(final String name) {
		try {

			final CacheResponse response = objectMapper.readValue(
					requestFactory.buildPostRequest(new ClearCacheUrl(hostName, projectId, name), new EmptyContent())
							.execute().getContent(), CacheResponse.class);

			validate(response, "cleared");

			log.debug("Successful request to clear to cache {}", name);

		} catch (final HTTPException e) {
			if (e.getStatusCode() == 301) {
				// Swallow this since that is what happens when cleared (it
				// isn't documented in the API spec)
				return;
			}
			throw Throwables.propagate(e);

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#deleteCache(java.lang
	 * .String)
	 */
	@Override
	public void deleteCache(final String name) {
		try {

			final CacheResponse response = objectMapper.readValue(
					requestFactory.buildDeleteRequest(new CacheUrl(hostName, projectId, name)).execute().getContent(),
					CacheResponse.class);

			validate(response, "deleted");

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#deleteItem(java.lang.
	 * String, java.lang.String)
	 */
	@Override
	public void deleteItem(final String name, final String key) {
		try {

			final CacheResponse response = objectMapper.readValue(
					requestFactory.buildDeleteRequest(new CacheItemUrl(hostName, projectId, name, key)).execute()
							.getContent(), CacheResponse.class);

			validate(response, "deleted");

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#getCache(java.lang.String
	 * )
	 */
	@Override
	public Optional<Cache> getCache(final String name) {
		try {

			return Optional.fromNullable(objectMapper.readValue(
					requestFactory.buildGetRequest(new CacheUrl(hostName, projectId, name)).execute().getContent(),
					Cache.class));

		} catch (final HTTPException e) {
			if (e.getStatusCode() == 404) {
				return Optional.absent();
			}
			throw Throwables.propagate(e);

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#getItem(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Optional<CachedItem> getItem(final String name, final String key) {
		try {

			return Optional.fromNullable(objectMapper.readValue(
					requestFactory.buildGetRequest(new CacheItemUrl(hostName, projectId, name, key)).execute()
							.getContent(), CachedItem.class));

		} catch (final HTTPException e) {
			if (e.getStatusCode() == 404) {
				return Optional.absent();
			}
			throw Throwables.propagate(e);

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#incrementItem(java.lang
	 * .String, java.lang.String, int)
	 */
	@Override
	public void incrementItem(final String name, final String key, final int amount) {
		try {

			final IncrementCacheResponse response = objectMapper.readValue(
					requestFactory
							.buildPostRequest(new IncrementCacheItemUrl(hostName, projectId, name, key),
									new JsonHttpContent(JSON_FACTORY, new Increment().amount(amount))).execute()
							.getContent(), IncrementCacheResponse.class);

			// Validate an increment, presence of a value is validation
			if (null == response.getValue()) {
				String message = MoreObjects.firstNonNull(
						response.getMessage(), "Increment value returned NULL");
				throw new IllegalArgumentException(message);
			}

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.mrcritical.ironcache.SimpleIronCache#listCaches()
	 */
	@Override
	public List<Cache> listCaches() {
		try {

			return objectMapper.readValue(requestFactory.buildGetRequest(new CachesUrl(hostName, projectId)).execute()
					.getContent(), new TypeReference<List<Cache>>() {
			});

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.mrcritical.ironcache.SimpleIronCache#putItem(java.lang.String,
	 * com.github.mrcritical.ironcache.model.CacheItemRequest)
	 */
	@Override
	public void putItem(final String name, final CacheItemRequest request) {
		try {
			validate(request);
			final CacheRequest item = new CacheRequest().value(request.getValue()).cas(request.getCas())
					.expires(request.getExpireAfter());
			// .replace(request.getOnlyIfExists())
			// .add(request.getOnlyIfNotExists());

			final CacheResponse response = objectMapper.readValue(
					requestFactory
							.buildPutRequest(new CacheItemUrl(hostName, projectId, name, request.getKey()),
									new JsonHttpContent(JSON_FACTORY, item)).execute().getContent(),
					CacheResponse.class);

			validate(response, "stored");

			log.debug("Successful request to add item {} with value {} to the cache {}", request.getKey(),
					request.getValue(), name);

		} catch (final Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Validates that the request being made is going to end up in a valid
	 * request when processed on the server. This includes:
	 *
	 * <ul>
	 * <li>Verifying that a key was provided that is not blank</li>
	 * <li>Verifying that a NON-NULL value was provided</li>
	 * <li>That {@link CacheItemRequest#getOnlyIfExists()} and
	 * {@link CacheItemRequest#getOnlyIfNotExists()} are not both set (that
	 * negate each other)</li>
	 * <li>Verifying that any expiration provided is greater then 0</li>
	 * </ul>
	 *
	 * @param request
	 *            is the request to validate
	 */
	private void validate(final CacheItemRequest request) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(request.getKey()), "A valid key must be supplied");
		Preconditions.checkArgument(null != request.getValue(),
				"A valid value must be supplied, it maybe an empty string");
		Preconditions.checkArgument(!(null != request.getOnlyIfExists() && null != request.getOnlyIfNotExists()),
				"Cannot specify both Only If Exists and Only If Not Exists. The two conditions negate each other.");
		Preconditions.checkArgument(
				null == request.getExpireAfter() || null != request.getExpireAfter() && request.getExpireAfter() > 0,
				"An expiration value must be greater then 0");
	}

	/**
	 * Validates that the keyword expected was returned. Otherwise throws an
	 * exception with the message returned included.
	 *
	 * @param response
	 *            is the response to check
	 * @param keyword
	 *            is the keyword to expected
	 */
	private void validate(final CacheResponse response, final String keyword) {
		if (!response.getMessage().toLowerCase(Locale.US).startsWith(keyword)) {
			throw new IllegalArgumentException(response.getMessage());
		}
	}

}
