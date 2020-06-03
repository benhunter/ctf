package org.springframework.format;

import java.lang.annotation.Annotation;
import org.springframework.core.convert.converter.ConverterRegistry;

public interface FormatterRegistry extends ConverterRegistry {
  void addFormatter(Formatter<?> paramFormatter);
  
  void addFormatterForFieldType(Class<?> paramClass, Formatter<?> paramFormatter);
  
  void addFormatterForFieldType(Class<?> paramClass, Printer<?> paramPrinter, Parser<?> paramParser);
  
  void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> paramAnnotationFormatterFactory);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/FormatterRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */