/**
 *
 */
package com.github.mrcritical.ironcache.internal.http;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import com.github.mrcritical.ironcache.model.HTTPException;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.ExponentialBackOff;

/**
 * Response initializer that registers itself as to handle unsuccessful
 * responses. This will ensure a {@link HTTPException} is thrown instead of the
 * default exception type.
 *
 * @author pjarrell
 *
 */
@Slf4j
public class ResponseInitializer implements HttpRequestInitializer, HttpUnsuccessfulResponseHandler {

	private final JsonFactory jsonFactory;

	private final String token;

	/**
	 * Create a response initializer that uses the authentication token
	 * provided.
	 *
	 * @param token
	 *            is the authentication token
	 * @param jsonFactory
	 *            is the JSON factory to use
	 */
	public ResponseInitializer(final String token, final JsonFactory jsonFactory) {
		this.token = token;
		this.jsonFactory = jsonFactory;
	}

	/**
	 * Configures each request. This does 3 things:
	 * <ul>
	 * <li>Adds the authorization token to each request</li>
	 * <li>Adds the JSON factory to the request</li>
	 * <li>Adds exponential back off capability</li>
	 * </ul>
	 *
	 * @param request
	 *            is the request to configure
	 */
	protected void configure(final HttpRequest request) {
		request.getHeaders().setAuthorization(String.format("OAuth %s", token));
		request.setParser(new JsonObjectParser(jsonFactory));

		final ExponentialBackOff backoff = new ExponentialBackOff.Builder().setInitialIntervalMillis(500)
				.setMaxElapsedTimeMillis(60000).setMaxIntervalMillis(30000).setMultiplier(1.5)
				.setRandomizationFactor(0.5).build();
		request.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(backoff));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.api.client.http.HttpUnsuccessfulResponseHandler#handleResponse
	 * (com.google.api.client.http.HttpRequest,
	 * com.google.api.client.http.HttpResponse, boolean)
	 */
	@Override
	public boolean handleResponse(final HttpRequest request, final HttpResponse response, final boolean supportsRetry)
			throws IOException {
		log.trace("HTTP request {} resulted in {} with response {} and headers {}", request.getUrl().toString(),
				response.getStatusCode(), response.getStatusMessage(), response.getHeaders().toString());
		throw new HTTPException(response.getStatusCode(), response.getStatusMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.api.client.http.HttpRequestInitializer#initialize(com.google
	 * .api.client.http.HttpRequest)
	 */
	@Override
	public void initialize(final HttpRequest request) throws IOException {
		configure(request);
		request.setUnsuccessfulResponseHandler(this);
	}

}
