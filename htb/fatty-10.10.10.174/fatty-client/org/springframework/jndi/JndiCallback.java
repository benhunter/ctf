package org.springframework.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface JndiCallback<T> {
  @Nullable
  T doInContext(Context paramContext) throws NamingException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */