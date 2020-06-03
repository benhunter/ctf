package org.springframework.validation;

import org.springframework.lang.Nullable;

public interface MessageCodesResolver {
  String[] resolveMessageCodes(String paramString1, String paramString2);
  
  String[] resolveMessageCodes(String paramString1, String paramString2, String paramString3, @Nullable Class<?> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/MessageCodesResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */