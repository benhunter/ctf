package org.springframework.objenesis.instantiator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Instantiator {
  Typology value();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/objenesis/instantiator/annotations/Instantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */