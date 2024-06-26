package org.springframework.web.servlet.mvc.condition;

import org.springframework.lang.Nullable;

public interface NameValueExpression<T> {
  String getName();
  
  @Nullable
  T getValue();
  
  boolean isNegated();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/NameValueExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */