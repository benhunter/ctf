package org.springframework.web.servlet.resource;

import org.springframework.lang.Nullable;

public interface VersionPathStrategy {
  @Nullable
  String extractVersion(String paramString);
  
  String removeVersion(String paramString1, String paramString2);
  
  String addVersion(String paramString1, String paramString2);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/VersionPathStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */