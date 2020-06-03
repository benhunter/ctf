package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SessionAttributes {
  @AliasFor("names")
  String[] value() default {};
  
  @AliasFor("value")
  String[] names() default {};
  
  Class<?>[] types() default {};
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/SessionAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */