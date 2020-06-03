package org.springframework.http.codec.multipart;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

public interface Part {
  String name();
  
  HttpHeaders headers();
  
  Flux<DataBuffer> content();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/multipart/Part.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */