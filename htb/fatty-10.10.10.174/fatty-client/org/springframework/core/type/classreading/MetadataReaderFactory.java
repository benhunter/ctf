package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface MetadataReaderFactory {
  MetadataReader getMetadataReader(String paramString) throws IOException;
  
  MetadataReader getMetadataReader(Resource paramResource) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/MetadataReaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */