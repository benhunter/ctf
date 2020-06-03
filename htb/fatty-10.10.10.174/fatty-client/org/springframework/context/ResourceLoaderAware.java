package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

public interface ResourceLoaderAware extends Aware {
  void setResourceLoader(ResourceLoader paramResourceLoader);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/ResourceLoaderAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */