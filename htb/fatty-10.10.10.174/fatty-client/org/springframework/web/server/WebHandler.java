package org.springframework.web.server;

import reactor.core.publisher.Mono;

public interface WebHandler {
  Mono<Void> handle(ServerWebExchange paramServerWebExchange);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/WebHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */