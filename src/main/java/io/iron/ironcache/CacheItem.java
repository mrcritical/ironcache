package io.iron.ironcache;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CacheItem {

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private final String cache;

	private final long cas;

	private final LocalDateTime expires;

	private final int flags;

	private final String key;

	private final String token;

	private final String value;

	public CacheItem(final String value) {
		super();
		this.value = value;
		cache = null;
		cas = -1;
		expires = null;
		flags = -1;
		key = null;
		token = null;
	}

	public CacheItem(final String cache, final long cas, final LocalDateTime expires, final int flags,
			final String key, final String token, final String value) {
		super();
		this.cache = cache;
		this.cas = cas;
		this.expires = expires;
		this.flags = flags;
		this.key = key;
		this.token = token;
		this.value = value;
	}

	public CacheItem(final String cache, final long cas, final String expires, final int flags, final String key,
			final String token, final String value) {
		super();
		this.cache = cache;
		this.cas = cas;
		this.expires = formatter.parseLocalDateTime(expires);
		this.flags = flags;
		this.key = key;
		this.token = token;
		this.value = value;
	}

	/**
	 * @param cache
	 *            the cache to set
	 */
	public CacheItem cache(final String cache) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/**
	 * @param cas
	 *            the cas to set
	 */
	public CacheItem cas(final long cas) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CacheItem other = (CacheItem) obj;
		if (cache == null) {
			if (other.cache != null) {
				return false;
			}
		} else if (!cache.equals(other.cache)) {
			return false;
		}
		if (cas != other.cas) {
			return false;
		}
		if (expires == null) {
			if (other.expires != null) {
				return false;
			}
		} else if (!expires.equals(other.expires)) {
			return false;
		}
		if (flags != other.flags) {
			return false;
		}
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (token == null) {
			if (other.token != null) {
				return false;
			}
		} else if (!token.equals(other.token)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * @param expires
	 *            the expires to set
	 */
	public CacheItem expires(final LocalDateTime expires) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/**
	 * @param expires
	 *            the expires to set
	 */
	public CacheItem expires(final String expires) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public CacheItem flags(final int flags) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/**
	 * @return the cache
	 */
	public String getCache() {
		return cache;
	}

	/**
	 * @return the cas
	 */
	public long getCas() {
		return cas;
	}

	/**
	 * @return the expires
	 */
	public LocalDateTime getExpires() {
		return expires;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cache == null) ? 0 : cache.hashCode());
		result = prime * result + (int) (cas ^ (cas >>> 32));
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + flags;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public CacheItem key(final String key) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public CacheItem token(final String token) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheItem [");
		if (cache != null) {
			builder.append("cache=");
			builder.append(cache);
			builder.append(", ");
		}
		builder.append("cas=");
		builder.append(cas);
		builder.append(", ");
		if (expires != null) {
			builder.append("expires=");
			builder.append(formatter.print(expires));
			builder.append(", ");
		}
		builder.append("flags=");
		builder.append(flags);
		builder.append(", ");
		if (key != null) {
			builder.append("key=");
			builder.append(key);
			builder.append(", ");
		}
		if (token != null) {
			builder.append("token=");
			builder.append(token);
			builder.append(", ");
		}
		if (value != null) {
			builder.append("value=");
			builder.append(value);
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public CacheItem value(final String value) {
		return new CacheItem(cache, cas, expires, flags, key, token, value);
	}

}
