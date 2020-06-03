package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface SourceExtractor {
  @Nullable
  Object extractSource(Object paramObject, @Nullable Resource paramResource);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/SourceExtractor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */