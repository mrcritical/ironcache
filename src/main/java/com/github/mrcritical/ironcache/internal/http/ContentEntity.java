/**
 * 
 */
package com.github.mrcritical.ironcache.internal.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;

/**
 * @author Yaniv Inbar
 *
 */
public class ContentEntity extends AbstractHttpEntity {

	private final long contentLength;

	private final StreamingContent streamingContent;

	/**
	 * @param contentLength
	 *            content length or less than zero if not known
	 * @param streamingContent
	 *            streaming content
	 */
	public ContentEntity(long contentLength, StreamingContent streamingContent) {
		this.contentLength = contentLength;
		this.streamingContent = Preconditions.checkNotNull(streamingContent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.HttpEntity#getContent()
	 */
	@Override
	public InputStream getContent() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.HttpEntity#getContentLength()
	 */
	@Override
	public long getContentLength() {
		return contentLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.HttpEntity#isRepeatable()
	 */
	@Override
	public boolean isRepeatable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.HttpEntity#isStreaming()
	 */
	@Override
	public boolean isStreaming() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.HttpEntity#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(OutputStream out) throws IOException {
		if (contentLength != 0) {
			streamingContent.writeTo(out);
		}
	}

}
