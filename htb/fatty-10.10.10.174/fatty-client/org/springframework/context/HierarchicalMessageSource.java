package org.springframework.context;

import org.springframework.lang.Nullable;

public interface HierarchicalMessageSource extends MessageSource {
  void setParentMessageSource(@Nullable MessageSource paramMessageSource);
  
  @Nullable
  MessageSource getParentMessageSource();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/HierarchicalMessageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */