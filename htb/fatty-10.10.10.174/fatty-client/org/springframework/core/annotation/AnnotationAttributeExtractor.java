package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.lang.Nullable;

interface AnnotationAttributeExtractor<S> {
  Class<? extends Annotation> getAnnotationType();
  
  @Nullable
  Object getAnnotatedElement();
  
  S getSource();
  
  @Nullable
  Object getAttributeValue(Method paramMethod);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/annotation/AnnotationAttributeExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */