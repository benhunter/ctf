package org.springframework.cache;

import java.util.Collection;
import org.springframework.lang.Nullable;

public interface CacheManager {
  @Nullable
  Cache getCache(String paramString);
  
  Collection<String> getCacheNames();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/CacheManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */