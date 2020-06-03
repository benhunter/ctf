package org.springframework.aop.aspectj.annotation;

import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.lang.Nullable;

public interface MetadataAwareAspectInstanceFactory extends AspectInstanceFactory {
  AspectMetadata getAspectMetadata();
  
  @Nullable
  Object getAspectCreationMutex();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/aspectj/annotation/MetadataAwareAspectInstanceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */