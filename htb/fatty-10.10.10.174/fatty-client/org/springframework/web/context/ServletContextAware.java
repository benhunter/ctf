package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;

public interface ServletContextAware extends Aware {
  void setServletContext(ServletContext paramServletContext);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/ServletContextAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */