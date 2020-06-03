package org.springframework.web.servlet.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;

public interface FreeMarkerConfig {
  Configuration getConfiguration();
  
  TaglibFactory getTaglibFactory();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/freemarker/FreeMarkerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */