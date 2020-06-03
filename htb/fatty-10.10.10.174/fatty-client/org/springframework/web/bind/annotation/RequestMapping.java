package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {
  String name() default "";
  
  @AliasFor("path")
  String[] value() default {};
  
  @AliasFor("value")
  String[] path() default {};
  
  RequestMethod[] method() default {};
  
  String[] params() default {};
  
  String[] headers() default {};
  
  String[] consumes() default {};
  
  String[] produces() default {};
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/RequestMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */