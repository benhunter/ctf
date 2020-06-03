package org.springframework.http.server.reactive;

import reactor.core.publisher.Mono;

public interface HttpHandler {
  Mono<Void> handle(ServerHttpRequest paramServerHttpRequest, ServerHttpResponse paramServerHttpResponse);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/HttpHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */