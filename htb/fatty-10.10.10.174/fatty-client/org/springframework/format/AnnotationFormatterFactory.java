package org.springframework.format;

import java.util.Set;

public interface AnnotationFormatterFactory<A extends java.lang.annotation.Annotation> {
  Set<Class<?>> getFieldTypes();
  
  Printer<?> getPrinter(A paramA, Class<?> paramClass);
  
  Parser<?> getParser(A paramA, Class<?> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/AnnotationFormatterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */