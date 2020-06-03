package org.springframework.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheConfig {
  String[] cacheNames() default {};
  
  String keyGenerator() default "";
  
  String cacheManager() default "";
  
  String cacheResolver() default "";
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/annotation/CacheConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */