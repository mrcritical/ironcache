/**
 * 
 */
package com.github.mrcritical.ironcache.internal.http;

import java.io.IOException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.util.Preconditions;

/**
 * @author Yaniv Inbar
 *
 */
public class ApacheHttpRequest extends LowLevelHttpRequest {

	private final HttpClient httpClient;

	private final HttpRequestBase request;

	public ApacheHttpRequest(HttpClient httpClient, HttpRequestBase request) {
		this.httpClient = httpClient;
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.api.client.http.LowLevelHttpRequest#addHeader(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) throws IOException {
		request.addHeader(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpRequest#setTimeout(int, int)
	 */
	@Override
	public void setTimeout(int connectTimeout, int readTimeout) throws IOException {
		request.setConfig(RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout)
				.build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpRequest#execute()
	 */
	@Override
	public LowLevelHttpResponse execute() throws IOException {
		if (getStreamingContent() != null) {
			Preconditions.checkArgument(request instanceof HttpEntityEnclosingRequest,
					"Apache HTTP client does not support %s requests with content.", request.getRequestLine()
							.getMethod());
			ContentEntity entity = new ContentEntity(getContentLength(), getStreamingContent());
			entity.setContentEncoding(getContentEncoding());
			entity.setContentType(getContentType());
			((HttpEntityEnclosingRequest) request).setEntity(entity);
		}
		return new ApacheHttpResponse(request, httpClient.execute(request));
	}

}
