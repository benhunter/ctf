package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
  @Nullable
  HttpMethod getRequestMethod();
  
  HttpHeaders getRequestHeaders();
  
  @Nullable
  HttpHeaders getMultipartHeaders(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/MultipartHttpServletRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */