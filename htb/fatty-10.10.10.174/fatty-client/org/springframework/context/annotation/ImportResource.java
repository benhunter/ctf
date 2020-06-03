package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ImportResource {
  @AliasFor("locations")
  String[] value() default {};
  
  @AliasFor("value")
  String[] locations() default {};
  
  Class<? extends BeanDefinitionReader> reader() default BeanDefinitionReader.class;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ImportResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */