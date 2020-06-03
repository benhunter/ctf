package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import org.springframework.lang.Nullable;

public interface CacheOperationSource {
  @Nullable
  Collection<CacheOperation> getCacheOperations(Method paramMethod, @Nullable Class<?> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */