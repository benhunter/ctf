package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface WebApplicationInitializer {
  void onStartup(ServletContext paramServletContext) throws ServletException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/WebApplicationInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */