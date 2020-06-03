package org.springframework.web.server;

import reactor.core.publisher.Mono;

public interface WebFilterChain {
  Mono<Void> filter(ServerWebExchange paramServerWebExchange);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/WebFilterChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */