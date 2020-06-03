package org.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

public interface RequestCondition<T> {
  T combine(T paramT);
  
  @Nullable
  T getMatchingCondition(HttpServletRequest paramHttpServletRequest);
  
  int compareTo(T paramT, HttpServletRequest paramHttpServletRequest);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/RequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */