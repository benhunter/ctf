package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;

@FunctionalInterface
public interface MethodFilter {
  List<Method> filter(List<Method> paramList);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/MethodFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */