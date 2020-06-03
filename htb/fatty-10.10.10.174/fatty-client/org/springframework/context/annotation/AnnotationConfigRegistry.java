package org.springframework.context.annotation;

public interface AnnotationConfigRegistry {
  void register(Class<?>... paramVarArgs);
  
  void scan(String... paramVarArgs);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/AnnotationConfigRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */