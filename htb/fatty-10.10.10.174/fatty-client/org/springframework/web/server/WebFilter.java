package org.springframework.web.server;

import reactor.core.publisher.Mono;

public interface WebFilter {
  Mono<Void> filter(ServerWebExchange paramServerWebExchange, WebFilterChain paramWebFilterChain);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/WebFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */