package org.springframework.core.codec;

import java.util.List;
import java.util.Map;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Decoder<T> {
  boolean canDecode(ResolvableType paramResolvableType, @Nullable MimeType paramMimeType);
  
  Flux<T> decode(Publisher<DataBuffer> paramPublisher, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
  
  Mono<T> decodeToMono(Publisher<DataBuffer> paramPublisher, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
  
  List<MimeType> getDecodableMimeTypes();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */