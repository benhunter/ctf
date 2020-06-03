package org.springframework.http.converter;

import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T> {
  boolean canRead(Type paramType, @Nullable Class<?> paramClass, @Nullable MediaType paramMediaType);
  
  T read(Type paramType, @Nullable Class<?> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
  
  boolean canWrite(@Nullable Type paramType, Class<?> paramClass, @Nullable MediaType paramMediaType);
  
  void write(T paramT, @Nullable Type paramType, @Nullable MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/GenericHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */