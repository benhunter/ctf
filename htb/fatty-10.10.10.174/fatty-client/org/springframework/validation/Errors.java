package org.springframework.validation;

import java.util.List;
import org.springframework.lang.Nullable;

public interface Errors {
  public static final String NESTED_PATH_SEPARATOR = ".";
  
  String getObjectName();
  
  void setNestedPath(String paramString);
  
  String getNestedPath();
  
  void pushNestedPath(String paramString);
  
  void popNestedPath() throws IllegalStateException;
  
  void reject(String paramString);
  
  void reject(String paramString1, String paramString2);
  
  void reject(String paramString1, @Nullable Object[] paramArrayOfObject, @Nullable String paramString2);
  
  void rejectValue(@Nullable String paramString1, String paramString2);
  
  void rejectValue(@Nullable String paramString1, String paramString2, String paramString3);
  
  void rejectValue(@Nullable String paramString1, String paramString2, @Nullable Object[] paramArrayOfObject, @Nullable String paramString3);
  
  void addAllErrors(Errors paramErrors);
  
  boolean hasErrors();
  
  int getErrorCount();
  
  List<ObjectError> getAllErrors();
  
  boolean hasGlobalErrors();
  
  int getGlobalErrorCount();
  
  List<ObjectError> getGlobalErrors();
  
  @Nullable
  ObjectError getGlobalError();
  
  boolean hasFieldErrors();
  
  int getFieldErrorCount();
  
  List<FieldError> getFieldErrors();
  
  @Nullable
  FieldError getFieldError();
  
  boolean hasFieldErrors(String paramString);
  
  int getFieldErrorCount(String paramString);
  
  List<FieldError> getFieldErrors(String paramString);
  
  @Nullable
  FieldError getFieldError(String paramString);
  
  @Nullable
  Object getFieldValue(String paramString);
  
  @Nullable
  Class<?> getFieldType(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/Errors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */