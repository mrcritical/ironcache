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
 * An increment cache item request.
 *
 * @author pjarrell
 *
 */
@Slf4j
public class IncrementCacheItemUrl extends GenericUrl {

	/**
	 * @param hostName
	 * @param projectId
	 * @param cacheName
	 * @param key
	 * @throws URISyntaxException
	 */
	public IncrementCacheItemUrl(final String hostName, final String projectId, final String cacheName, final String key)
			throws URISyntaxException {
		super(new URIBuilder()
				.setScheme("https")
				.setHost(String.format(Constants.IRON_IO_HOST_TEMPLATE, hostName))
				.setPath(
						new StringBuilder().append("/1/projects/").append(projectId).append("/caches/")
								.append(cacheName).append("/items/").append(key).append("/increment").toString())
				.build());
		log.trace("Creating request {}", this.toString());
	}

}
