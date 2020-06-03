package org.springframework.web.server.session;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

public interface WebSessionManager {
  Mono<WebSession> getSession(ServerWebExchange paramServerWebExchange);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/WebSessionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */