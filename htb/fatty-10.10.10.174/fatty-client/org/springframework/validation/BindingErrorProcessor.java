package org.springframework.validation;

import org.springframework.beans.PropertyAccessException;

public interface BindingErrorProcessor {
  void processMissingFieldError(String paramString, BindingResult paramBindingResult);
  
  void processPropertyAccessException(PropertyAccessException paramPropertyAccessException, BindingResult paramBindingResult);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/BindingErrorProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */