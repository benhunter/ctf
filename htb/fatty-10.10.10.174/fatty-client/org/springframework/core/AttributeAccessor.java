package org.springframework.core;

import org.springframework.lang.Nullable;

public interface AttributeAccessor {
  void setAttribute(String paramString, @Nullable Object paramObject);
  
  @Nullable
  Object getAttribute(String paramString);
  
  @Nullable
  Object removeAttribute(String paramString);
  
  boolean hasAttribute(String paramString);
  
  String[] attributeNames();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/AttributeAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */