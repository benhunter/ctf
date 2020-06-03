package org.springframework.cache.annotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.Nullable;

public interface CachingConfigurer {
  @Nullable
  CacheManager cacheManager();
  
  @Nullable
  CacheResolver cacheResolver();
  
  @Nullable
  KeyGenerator keyGenerator();
  
  @Nullable
  CacheErrorHandler errorHandler();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CachingConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */