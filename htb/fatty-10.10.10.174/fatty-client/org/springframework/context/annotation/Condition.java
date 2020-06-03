package org.springframework.context.annotation;

import org.springframework.core.type.AnnotatedTypeMetadata;

@FunctionalInterface
public interface Condition {
  boolean matches(ConditionContext paramConditionContext, AnnotatedTypeMetadata paramAnnotatedTypeMetadata);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/Condition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */