package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {
  <T extends R> Converter<S, T> getConverter(Class<T> paramClass);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/converter/ConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */