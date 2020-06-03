package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspTagException;
import org.springframework.lang.Nullable;

public interface ArgumentAware {
  void addArgument(@Nullable Object paramObject) throws JspTagException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/ArgumentAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */