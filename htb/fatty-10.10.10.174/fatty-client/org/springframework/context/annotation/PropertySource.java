package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.io.support.PropertySourceFactory;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(PropertySources.class)
public @interface PropertySource {
  String name() default "";
  
  String[] value();
  
  boolean ignoreResourceNotFound() default false;
  
  String encoding() default "";
  
  Class<? extends PropertySourceFactory> factory() default PropertySourceFactory.class;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/PropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */