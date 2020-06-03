package org.springframework.cache.annotation;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.lang.Nullable;

public interface CacheAnnotationParser {
  @Nullable
  Collection<CacheOperation> parseCacheAnnotations(Class<?> paramClass);
  
  @Nullable
  Collection<CacheOperation> parseCacheAnnotations(Method paramMethod);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CacheAnnotationParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */