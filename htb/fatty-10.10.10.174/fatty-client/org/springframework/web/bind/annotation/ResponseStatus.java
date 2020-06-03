package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpStatus;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseStatus {
  @AliasFor("code")
  HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;
  
  @AliasFor("value")
  HttpStatus code() default HttpStatus.INTERNAL_SERVER_ERROR;
  
  String reason() default "";
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/annotation/ResponseStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */