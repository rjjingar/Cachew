# Cachew

Cachew is a lightweight Java library for adding in-memory local cache to your applications.

## Features
1. Time to live (ttl) support by default or for individual keys at the time of insertion. 
2. Support for multiple eviction policies. Currently implemented support for LRU and TTL based eviction. Note that clients can choose to implement their own eviction policy by implementing EvictionPolicy interface
3. Auto loading from origin by providing an implementation of CacheOrigin interface. This is optional and if provided to builder then keys missing in cache will be automatically loaded from this origin.


## Dependency

Add it to your application dependency

``` TO be added```

## Usage
### Basic usage
```Java
// Below is an example for creating cache for String key and String value
// Note that a Cachew object is statically tied to a fixed key and value type specified at creation time. 
// For different data types create different value types.
// Cachew<String, Json> could be used as a generic cache where Json value gets deserialized into POJO after retrieval from cache.  
// Simple use case (no eviction policies, no default ttl, no auto cache refresh)

Cachew<String, String> cachew = new CachewBuilder<String, String>().build();
cachew.putValue("key-1", "val-1");
String value = cachew.getValue("key-1"); // Assertions.assertEquals("val-1", value);
```

### With TTL 
```Java
// Using ttl
// globalTtlDuration is optional but recommended. This will apply to all keys unless overridden in putValue
CacheConfiguration config = CacheConfiguration.builder()
        .globalTtlDuration(Duration.ofSeconds(10))
        .build();

Cachew<String, String> cachew = new CachewBuilder<String, String>()
        .withCacheConfiguration(config)
        .build();

cachew.putValue("key-1", "val-1"); // Default ttl of 10 seconds will apply
cachew.putValue("key-2", "val-2", Duration.ofSeconds(1)); // custom ttl of 1 seconds will apply

// Both keys will exist
Assertions.assertEquals("val-1", cachew.getValue("key-1"));
Assertions.assertEquals("val-2", cachew.getValue("key-2"));

Thread.sleep(1000); // wait till ttl of key-2
Assertions.assertEquals("val-1", cachew.getValue("key-1")); // key-1 still in cache since its ttl has not expired yet.
// key-2 should have expired
CachewException keyError2 = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-2"));
Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, keyError2.getErrorCode());

Thread.sleep(9000); // wait till ttl of key-1
// key-1 should have expired
CachewException keyError1 = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-1"));
Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, keyError1.getErrorCode());
```

### With LRU
```Java
// Using LRU eviction policy
CacheConfiguration config = CacheConfiguration.builder()
        .maxKeys(100) // defaults to -1 for no limit. If a limit is specified then by default LRU cache eviction will apply
        .build();

Cachew<String, String> cachew = new CachewBuilder<String, String>()
        .withCacheConfiguration(config)
        .build();

for (int i = 1; i <= 100; i++) {
    // key-1 to key-100
    cachew.putValue("key-" + i, "val-" + i);
}
for (int i = 1; i <= 100; i++) {
    // All keys should be present since cache has not breached its max size
    Assertions.assertEquals("val-" + i, cachew.getValue("key-" + i));
}
for (int i = 101; i <= 200; i++) {
    // key-101 to key-200
    cachew.putValue("key-" + i, "val-" + i);
    // key-1 to key-100 will get evicted sequentially
    String evictedKey = "key-" + (i - 100);
    CachewException evictedKeyError = Assertions.assertThrows(CachewException.class, () -> cachew.getValue(evictedKey));
    Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, evictedKeyError.getErrorCode());
}
```

### Cache Origin Implementation 
```Java
// Cachew supports auto-loading of keys from origin if they are missing in cache
// For this clients need to implement the CacheOrigin<K, V> interface like below
public class MyCacheOrigin implements CacheOrigin<String, String> {
    private final Map<String, String> storageMap;

    public MyCacheOrigin() {
        this.storageMap = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            this.storageMap.put("key-" + i, "val-" + i);
        }
    }
    @Override
    public Optional<String> retrieveValue(String key) throws OriginException {
        // call some remote service or fetch from a database.
        // in this example we are fetching values from a hash map.
        if (!storageMap.containsKey(key)) {
            throw new OriginException(OriginException.OriginErrorCode.KEY_NOT_FOUND_IN_ORIGIN);
        }
        return Optional.of(storageMap.get(key));
    }
}

@Test
public void withCacheOrigin() throws CachewException {
    Cachew<String, String> cachew = new CachewBuilder<String, String>()
            .withCacheOrigin(new MyCacheOrigin())
            .build();
    for (int i = 1; i <= 100; i++) {
        // All keys should be present since origin has keys from key-1 to key-100
        Assertions.assertEquals("val-" + i, cachew.getValue("key-" + i));
    }
    // key-101 is not in origin
    CachewException missingKeyError = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-101"));
    Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_ORIGIN, missingKeyError.getErrorCode());
}



```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)