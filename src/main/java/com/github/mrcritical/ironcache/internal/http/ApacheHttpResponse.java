/**
 * 
 */
package com.github.mrcritical.ironcache.internal.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;

import com.google.api.client.http.LowLevelHttpResponse;

/**
 * @author pjarrell
 *
 */
public class ApacheHttpResponse extends LowLevelHttpResponse {

	private final Header[] allHeaders;

	private final HttpRequestBase request;

	private final HttpResponse response;

	/**
	 * 
	 */
	public ApacheHttpResponse(HttpRequestBase request, HttpResponse response) {
		this.request = request;
		this.response = response;
		allHeaders = response.getAllHeaders();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#disconnect()
	 */
	@Override
	public void disconnect() {
		request.abort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getContent()
	 */
	@Override
	public InputStream getContent() throws IOException {
		HttpEntity entity = response.getEntity();
		return entity == null ? null : entity.getContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getContentEncoding()
	 */
	@Override
	public String getContentEncoding() {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Header contentEncodingHeader = entity.getContentEncoding();
			if (contentEncodingHeader != null) {
				return contentEncodingHeader.getValue();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getContentLength()
	 */
	@Override
	public long getContentLength() {
		HttpEntity entity = response.getEntity();
		return entity == null ? -1 : entity.getContentLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getContentType()
	 */
	@Override
	public String getContentType() {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Header contentTypeHeader = entity.getContentType();
			if (contentTypeHeader != null) {
				return contentTypeHeader.getValue();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getHeaderCount()
	 */
	@Override
	public int getHeaderCount() {
		return allHeaders.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getHeaderName(int)
	 */
	@Override
	public String getHeaderName(int index) {
		return allHeaders[index].getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getHeaderValue(int)
	 */
	@Override
	public String getHeaderValue(int index) {
		return allHeaders[index].getValue();
	}

	public String getHeaderValue(String name) {
		return response.getLastHeader(name).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getReasonPhrase()
	 */
	@Override
	public String getReasonPhrase() {
		StatusLine statusLine = response.getStatusLine();
		return statusLine == null ? null : statusLine.getReasonPhrase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getStatusCode()
	 */
	@Override
	public int getStatusCode() throws IOException {
		StatusLine statusLine = response.getStatusLine();
		return statusLine == null ? 0 : statusLine.getStatusCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.api.client.http.LowLevelHttpResponse#getStatusLine()
	 */
	@Override
	public String getStatusLine() {
		StatusLine statusLine = response.getStatusLine();
		return statusLine == null ? null : statusLine.toString();
	}

}
