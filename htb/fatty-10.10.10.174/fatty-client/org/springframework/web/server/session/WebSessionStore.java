package org.springframework.web.server.session;

import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

public interface WebSessionStore {
  Mono<WebSession> createWebSession();
  
  Mono<WebSession> retrieveSession(String paramString);
  
  Mono<Void> removeSession(String paramString);
  
  Mono<WebSession> updateLastAccessTime(WebSession paramWebSession);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/session/WebSessionStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */