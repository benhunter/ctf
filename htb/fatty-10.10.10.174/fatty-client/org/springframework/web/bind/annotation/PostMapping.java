package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = {RequestMethod.POST})
public @interface PostMapping {
  @AliasFor(annotation = RequestMapping.class)
  String name() default "";
  
  @AliasFor(annotation = RequestMapping.class)
  String[] value() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] path() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] params() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] headers() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] consumes() default {};
  
  @AliasFor(annotation = RequestMapping.class)
  String[] produces() default {};
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/PostMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */