package org.springframework.beans;

import org.springframework.lang.Nullable;

public interface Mergeable {
  boolean isMergeEnabled();
  
  Object merge(@Nullable Object paramObject);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/Mergeable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */