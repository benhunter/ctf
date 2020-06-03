package org.springframework.remoting.httpinvoker;

import org.springframework.lang.Nullable;

public interface HttpInvokerClientConfiguration {
  String getServiceUrl();
  
  @Nullable
  String getCodebaseUrl();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/HttpInvokerClientConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */