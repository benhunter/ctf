package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

public interface CacheErrorHandler {
  void handleCacheGetError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  void handleCachePutError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject1, @Nullable Object paramObject2);
  
  void handleCacheEvictError(RuntimeException paramRuntimeException, Cache paramCache, Object paramObject);
  
  void handleCacheClearError(RuntimeException paramRuntimeException, Cache paramCache);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */