package org.springframework.context.annotation;

import org.springframework.beans.factory.Aware;
import org.springframework.core.type.AnnotationMetadata;

public interface ImportAware extends Aware {
  void setImportMetadata(AnnotationMetadata paramAnnotationMetadata);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ImportAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */