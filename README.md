Iron.io IronCache
=========

Java Client for Iron.io IronCache.

Maven Dependency
=========

``` xml
<dependency>
  <groupId>io.iron</groupId>
  <artifactId>ironcache</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Examples
=========

Add a cache item:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.put("<key>", "<value>");
```

Get a cache item:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;
import io.iron.ironcache.CacheItem;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
CacheItem item = cache.get("<key>");
// or
CacheItem item = cache.getItem("<cacheName>", "<key>");
System.out.println(item.getValue());
```

Increment an item in the cache:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.increment("<key>", 2);
// or
cache.incrementItem("<cacheName>", "<key>", 2);
```

Replace a cache item:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.put("<key>", "<new_value>", false, true);
// or
cache.putItem("<cacheName>", "<key>", "<new_value>", false, true);
```

Delete a cache item:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;

IronCache cache = new DefaultIronCache("<token>", "<projectId>", "<cacheName>");
cache.delete("<key>");
```
	
Get list of available caches:

``` java
import io.iron.ironcache.IronCache;
import io.iron.ironcache.DefaultIronCache;
import io.iron.ironcache.Cache;

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