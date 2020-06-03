package org.springframework.context;

import org.springframework.beans.factory.Aware;

public interface MessageSourceAware extends Aware {
  void setMessageSource(MessageSource paramMessageSource);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/MessageSourceAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */