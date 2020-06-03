package org.springframework.web.server;

import reactor.core.publisher.Mono;

public interface WebExceptionHandler {
  Mono<Void> handle(ServerWebExchange paramServerWebExchange, Throwable paramThrowable);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/WebExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */