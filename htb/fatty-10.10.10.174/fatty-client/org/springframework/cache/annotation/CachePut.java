package org.springframework.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CachePut {
  @AliasFor("cacheNames")
  String[] value() default {};
  
  @AliasFor("value")
  String[] cacheNames() default {};
  
  String key() default "";
  
  String keyGenerator() default "";
  
  String cacheManager() default "";
  
  String cacheResolver() default "";
  
  String condition() default "";
  
  String unless() default "";
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CachePut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */