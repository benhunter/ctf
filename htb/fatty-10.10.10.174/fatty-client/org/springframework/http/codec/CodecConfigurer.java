package org.springframework.http.codec;

import java.util.List;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;

public interface CodecConfigurer {
  DefaultCodecs defaultCodecs();
  
  CustomCodecs customCodecs();
  
  void registerDefaults(boolean paramBoolean);
  
  List<HttpMessageReader<?>> getReaders();
  
  List<HttpMessageWriter<?>> getWriters();
  
  public static interface CustomCodecs {
    void decoder(Decoder<?> param1Decoder);
    
    void encoder(Encoder<?> param1Encoder);
    
    void reader(HttpMessageReader<?> param1HttpMessageReader);
    
    void writer(HttpMessageWriter<?> param1HttpMessageWriter);
  }
  
  public static interface DefaultCodecs {
    void jackson2JsonDecoder(Decoder<?> param1Decoder);
    
    void jackson2JsonEncoder(Encoder<?> param1Encoder);
    
    void protobufDecoder(Decoder<?> param1Decoder);
    
    void protobufEncoder(Encoder<?> param1Encoder);
    
    void jaxb2Decoder(Decoder<?> param1Decoder);
    
    void jaxb2Encoder(Encoder<?> param1Encoder);
    
    void enableLoggingRequestDetails(boolean param1Boolean);
  }
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/CodecConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */