package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

@FunctionalInterface
public interface ScopeMetadataResolver {
  ScopeMetadata resolveScopeMetadata(BeanDefinition paramBeanDefinition);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ScopeMetadataResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */