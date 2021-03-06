/**
 *
 */
package com.github.mrcritical.ironcache.internal.model.requests;

import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.utils.URIBuilder;

import com.github.mrcritical.ironcache.internal.model.Constants;
import com.google.api.client.http.GenericUrl;

/**
 * A request that deals with a particular cache.
 *
 * @author pjarrell
 *
 */
@Slf4j
public class CacheUrl extends GenericUrl {

	/**
	 * @param hostName
	 * @param projectId
	 * @param cacheName
	 * @throws URISyntaxException
	 */
	public CacheUrl(final String hostName, final String projectId, final String cacheName) throws URISyntaxException {
		super(new URIBuilder()
				.setScheme("https")
				.setHost(String.format(Constants.IRON_IO_HOST_TEMPLATE, hostName))
				.setPath(
						new StringBuilder().append("/1/projects/").append(projectId).append("/caches/")
								.append(cacheName).toString()).build());
		log.trace("Creating request {}", this.toString());
	}

}
