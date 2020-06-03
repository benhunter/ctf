package org.springframework.http;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface ReactiveHttpInputMessage extends HttpMessage {
  Flux<DataBuffer> getBody();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ReactiveHttpInputMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */