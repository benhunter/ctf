package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;

public interface EnvironmentAware extends Aware {
  void setEnvironment(Environment paramEnvironment);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/EnvironmentAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */