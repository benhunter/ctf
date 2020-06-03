package org.springframework.cache.interceptor;

import java.util.Collection;
import org.springframework.cache.Cache;

@FunctionalInterface
public interface CacheResolver {
  Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> paramCacheOperationInvocationContext);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */