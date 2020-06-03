package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

interface ImportRegistry {
  @Nullable
  AnnotationMetadata getImportingClassFor(String paramString);
  
  void removeImportingClass(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ImportRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */