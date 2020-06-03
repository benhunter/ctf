package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.jmx.support.RegistrationPolicy;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MBeanExportConfiguration.class})
public @interface EnableMBeanExport {
  String defaultDomain() default "";
  
  String server() default "";
  
  RegistrationPolicy registration() default RegistrationPolicy.FAIL_ON_EXISTING;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/EnableMBeanExport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */