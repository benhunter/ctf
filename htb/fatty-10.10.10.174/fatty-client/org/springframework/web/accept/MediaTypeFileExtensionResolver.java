package org.springframework.web.accept;

import java.util.List;
import org.springframework.http.MediaType;

public interface MediaTypeFileExtensionResolver {
  List<String> resolveFileExtensions(MediaType paramMediaType);
  
  List<String> getAllFileExtensions();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/MediaTypeFileExtensionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */