Iron.io IronCache
=========

Java Client for Iron.io IronCache.

This new version fixes several issues, including handling for UTF-8. It also exposes expiration and other new API endpoints. It has also been made thread-safe.

Maven Dependency
=========

``` xml
<dependency>
  <groupId>com.github.mrcritical</groupId>
  <artifactId>ironcache</artifactId>
  <version>2.0.0-SNAPSHOT</version>
</dependency>
```

Examples
=========

Add a cache item:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;
import com.github.mrcritical.ironcache.CacheItemRequest;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");

final CacheItemRequest request = CacheItemRequest.create().key("<key>").value("<value>");
cacheProvider.putItem("<cacheName>", request);
```

Get a cache item:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;
import com.github.mrcritical.ironcache.CacheItemRequest;
import com.github.mrcritical.ironcache.CacheItem;
import com.google.common.base.Optional;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");

final CacheItemRequest request = CacheItemRequest.create().key("<key>").value("<value>");
Optional<CacheItem> item = cacheProvider.getItem("<cacheName>", request);
if(item.isPresent()) {
	System.out.println(item.get().getValue());
}
```

Increment an item in the cache:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");
cacheProvider.incrementItem("<cacheName>", "<key">, 1);
```

Replace a cache item:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;
import com.github.mrcritical.ironcache.CacheItemRequest;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");

final CacheItemRequest request = CacheItemRequest.create().key("<key>").value("<value>");
cacheProvider.putItem("<cacheName>", request);
```

Delete a cache item:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");

cacheProvider.deleteItem("<cacheName>", "<key>");
```
	
Get list of available caches:

``` java
import com.github.mrcritical.ironcache.IronCacheProvider;
import com.github.mrcritical.ironcache.DefaultIronCacheProvider;
import java.util.List;

IronCacheProvider cacheProvider = new DefaultIronCacheProvider("<token>", "<projectId>");
List<Cache> caches = cacheProvider.listCaches();
```
	
Dependencies
=========
- Joda (for date/time)
- Faster Jackson (for json)
- Commons HTTP Client
- Commons IO
- Google HTTP Client
- Guava (for argument checks)
- SLF4J

Test Dependencies
=========
- Junit
- Mockito