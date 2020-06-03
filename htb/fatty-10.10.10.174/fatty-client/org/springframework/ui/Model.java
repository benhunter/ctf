package org.springframework.ui;

import java.util.Collection;
import java.util.Map;
import org.springframework.lang.Nullable;

public interface Model {
  Model addAttribute(String paramString, @Nullable Object paramObject);
  
  Model addAttribute(Object paramObject);
  
  Model addAllAttributes(Collection<?> paramCollection);
  
  Model addAllAttributes(Map<String, ?> paramMap);
  
  Model mergeAttributes(Map<String, ?> paramMap);
  
  boolean containsAttribute(String paramString);
  
  Map<String, Object> asMap();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/ui/Model.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */