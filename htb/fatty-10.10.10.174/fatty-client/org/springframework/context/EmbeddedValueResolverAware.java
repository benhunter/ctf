package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.util.StringValueResolver;

public interface EmbeddedValueResolverAware extends Aware {
  void setEmbeddedValueResolver(StringValueResolver paramStringValueResolver);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/EmbeddedValueResolverAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */