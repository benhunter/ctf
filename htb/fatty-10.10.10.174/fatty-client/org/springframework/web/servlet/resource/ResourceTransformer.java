package org.springframework.web.servlet.resource;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

@FunctionalInterface
public interface ResourceTransformer {
  Resource transform(HttpServletRequest paramHttpServletRequest, Resource paramResource, ResourceTransformerChain paramResourceTransformerChain) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */