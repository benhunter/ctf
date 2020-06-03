package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPart {
  @AliasFor("name")
  String value() default "";
  
  @AliasFor("value")
  String name() default "";
  
  boolean required() default true;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/RequestPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */