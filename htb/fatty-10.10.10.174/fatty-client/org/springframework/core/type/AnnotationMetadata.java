package org.springframework.core.type;

import java.util.Set;

public interface AnnotationMetadata extends ClassMetadata, AnnotatedTypeMetadata {
  Set<String> getAnnotationTypes();
  
  Set<String> getMetaAnnotationTypes(String paramString);
  
  boolean hasAnnotation(String paramString);
  
  boolean hasMetaAnnotation(String paramString);
  
  boolean hasAnnotatedMethods(String paramString);
  
  Set<MethodMetadata> getAnnotatedMethods(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/AnnotationMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */