package org.springframework.web.multipart;

import javax.servlet.http.HttpServletRequest;

public interface MultipartResolver {
  boolean isMultipart(HttpServletRequest paramHttpServletRequest);
  
  MultipartHttpServletRequest resolveMultipart(HttpServletRequest paramHttpServletRequest) throws MultipartException;
  
  void cleanupMultipart(MultipartHttpServletRequest paramMultipartHttpServletRequest);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/multipart/MultipartResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */