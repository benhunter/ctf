package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;

public interface ImportSelector {
  String[] selectImports(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ImportSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */