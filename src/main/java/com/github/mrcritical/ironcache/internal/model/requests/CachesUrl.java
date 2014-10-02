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
 * A list caches request.
 *
 * @author pjarrell
 *
 */
@Slf4j
public class CachesUrl extends GenericUrl {

	/**
	 * @param hostName
	 * @param projectId
	 * @throws URISyntaxException
	 */
	public CachesUrl(final String hostName, final String projectId) throws URISyntaxException {
		super(new URIBuilder().setScheme("https").setHost(String.format(Constants.IRON_IO_HOST_TEMPLATE, hostName))
				.setPath(new StringBuilder().append("/1/projects/").append(projectId).append("/caches").toString())
				.build());
		log.trace("Creating request {}", this.toString());
	}

}
