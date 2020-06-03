package org.springframework.core.convert;

import org.springframework.lang.Nullable;

public interface ConversionService {
  boolean canConvert(@Nullable Class<?> paramClass1, Class<?> paramClass2);
  
  boolean canConvert(@Nullable TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
  
  @Nullable
  <T> T convert(@Nullable Object paramObject, Class<T> paramClass);
  
  @Nullable
  Object convert(@Nullable Object paramObject, @Nullable TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/ConversionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */