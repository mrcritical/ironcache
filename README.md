Iron.io IronCache
=========

Java Client for Iron.io IronCache.

Maven Dependency
=========

``` xml
<dependency>
  <groupId>com.github.mrcritical</groupId>
  <artifactId>ironcache</artifactId>
  <version>1.0.0</version>
</dependency>
```

Examples
=========

Add a cache item:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.put("<key>", "<value>");
// or using another, non-default cache
cache.putItem("<cacheName>", "<key>", "<value>");
```

Get a cache item:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.CacheItem;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
CacheItem item = cache.get("<key>");
// or using another, non-default cache
CacheItem item = cache.getItem("<cacheName>", "<key>");
System.out.println(item.getValue());
```

Increment an item in the cache:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.increment("<key>", 2);
// or using another, non-default cache
cache.incrementItem("<cacheName>", "<key>", 2);
```

Replace a cache item:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.put("<key>", "<new_value>", false, true);
// or using another, non-default cache
cache.putItem("<cacheName>", "<key>", "<new_value>", false, true);
```

Delete a cache item:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.delete("<key>");
// or using another, non-default cache
cache.deleteItem("<cacheName>", "<key>");
```
	
Get list of available caches:

``` java
import com.github.mrcritical.ironcache.IronCache;
import com.github.mrcritical.ironcache.DefaultIronCache;
import com.github.mrcritical.ironcache.Cache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>");
List<Cache> caches = cache.getCaches();
```
	
Dependencies
=========
- Joda (for date/time)
- Jackson (for json)
- Commons HTTP Client
- Guava (for argument checks)

Test Dependencies
=========
- Spring Core (for classpath resources)
- Junit
- Mockito
