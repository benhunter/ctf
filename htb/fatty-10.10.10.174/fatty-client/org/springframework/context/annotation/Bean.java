package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
  @AliasFor("name")
  String[] value() default {};
  
  @AliasFor("value")
  String[] name() default {};
  
  @Deprecated
  Autowire autowire() default Autowire.NO;
  
  boolean autowireCandidate() default true;
  
  String initMethod() default "";
  
  String destroyMethod() default "(inferred)";
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/Bean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */