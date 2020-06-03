package org.springframework.beans.factory.support;

import java.security.AccessControlContext;

public interface SecurityContextProvider {
  AccessControlContext getAccessControlContext();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/SecurityContextProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */