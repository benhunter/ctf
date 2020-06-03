package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

public interface FlashMapManager {
  @Nullable
  FlashMap retrieveAndUpdate(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
  
  void saveOutputFlashMap(FlashMap paramFlashMap, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/FlashMapManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */