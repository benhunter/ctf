package org.springframework.http.codec;

import java.util.Map;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface HttpMessageDecoder<T> extends Decoder<T> {
  Map<String, Object> getDecodeHints(ResolvableType paramResolvableType1, ResolvableType paramResolvableType2, ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/HttpMessageDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */