package org.springframework.validation;

public interface Validator {
  boolean supports(Class<?> paramClass);
  
  void validate(Object paramObject, Errors paramErrors);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/Validator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */