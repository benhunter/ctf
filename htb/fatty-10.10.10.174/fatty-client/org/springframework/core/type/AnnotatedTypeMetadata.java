package org.springframework.core.type;

import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

public interface AnnotatedTypeMetadata {
  boolean isAnnotated(String paramString);
  
  @Nullable
  Map<String, Object> getAnnotationAttributes(String paramString);
  
  @Nullable
  Map<String, Object> getAnnotationAttributes(String paramString, boolean paramBoolean);
  
  @Nullable
  MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString);
  
  @Nullable
  MultiValueMap<String, Object> getAllAnnotationAttributes(String paramString, boolean paramBoolean);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/AnnotatedTypeMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */