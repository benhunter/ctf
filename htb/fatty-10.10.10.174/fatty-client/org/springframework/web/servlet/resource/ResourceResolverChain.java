package org.springframework.web.servlet.resource;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

public interface ResourceResolverChain {
  @Nullable
  Resource resolveResource(@Nullable HttpServletRequest paramHttpServletRequest, String paramString, List<? extends Resource> paramList);
  
  @Nullable
  String resolveUrlPath(String paramString, List<? extends Resource> paramList);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceResolverChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */