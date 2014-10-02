/**
 * 
 */
package com.github.mrcritical.ironcache.internal.http;

import java.io.IOException;
import java.net.ProxySelector;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;

/**
 * @author pjarrell
 *
 */
public class ApacheHttpTransport extends HttpTransport {

	private final CloseableHttpClient httpClient;

	public ApacheHttpTransport() {
		this(CachingHttpClients.custom().disableRedirectHandling()
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
				.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
				.setConnectionManager(new PoolingHttpClientConnectionManager()).build());
	}

	public ApacheHttpTransport(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.api.client.http.HttpTransport#buildRequest(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	protected LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
		HttpRequestBase requestBase;
		if (method.equals(HttpMethods.DELETE)) {
			requestBase = new HttpDelete(url);
		} else if (method.equals(HttpMethods.GET)) {
			requestBase = new HttpGet(url);
		} else if (method.equals(HttpMethods.HEAD)) {
			requestBase = new HttpHead(url);
		} else if (method.equals(HttpMethods.POST)) {
			requestBase = new HttpPost(url);
		} else if (method.equals(HttpMethods.PUT)) {
			requestBase = new HttpPut(url);
		} else if (method.equals(HttpMethods.TRACE)) {
			requestBase = new HttpTrace(url);
		} else if (method.equals(HttpMethods.OPTIONS)) {
			requestBase = new HttpOptions(url);
		} else {
			requestBase = new HttpExtensionMethod(method, url);
		}
		return new ApacheHttpRequest(httpClient, requestBase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.HttpTransport#shutdown()
	 */
	@Override
	public void shutdown() throws IOException {
		httpClient.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.api.client.http.HttpTransport#supportsMethod(java.lang.String)
	 */
	@Override
	public boolean supportsMethod(String method) throws IOException {
		return true;
	}

}
