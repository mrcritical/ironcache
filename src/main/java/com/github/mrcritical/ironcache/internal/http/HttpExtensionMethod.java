/**
 * 
 */
package com.github.mrcritical.ironcache.internal.http;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import com.google.api.client.util.Preconditions;

/**
 * HTTP extension method.
 *
 * @author Yaniv Inbar
 *
 */
public class HttpExtensionMethod extends HttpEntityEnclosingRequestBase {

	private final String methodName;

	/**
	 * @param methodName
	 * @param uri
	 */
	public HttpExtensionMethod(String methodName, String uri) {
		this.methodName = Preconditions.checkNotNull(methodName);
		setURI(URI.create(uri));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.methods.HttpRequestBase#getMethod()
	 */
	@Override
	public String getMethod() {
		return methodName;
	}

}
