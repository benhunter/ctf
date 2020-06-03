package org.springframework.web.util;

import java.net.URI;
import java.util.Map;

public interface UriTemplateHandler {
  URI expand(String paramString, Map<String, ?> paramMap);
  
  URI expand(String paramString, Object... paramVarArgs);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/UriTemplateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */