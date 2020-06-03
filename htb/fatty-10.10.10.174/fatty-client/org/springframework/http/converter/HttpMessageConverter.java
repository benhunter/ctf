package org.springframework.http.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

public interface HttpMessageConverter<T> {
  boolean canRead(Class<?> paramClass, @Nullable MediaType paramMediaType);
  
  boolean canWrite(Class<?> paramClass, @Nullable MediaType paramMediaType);
  
  List<MediaType> getSupportedMediaTypes();
  
  T read(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage) throws IOException, HttpMessageNotReadableException;
  
  void write(T paramT, @Nullable MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage) throws IOException, HttpMessageNotWritableException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/HttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */