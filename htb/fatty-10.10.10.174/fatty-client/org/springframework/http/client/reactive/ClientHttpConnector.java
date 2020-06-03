package org.springframework.http.client.reactive;

import java.net.URI;
import java.util.function.Function;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

public interface ClientHttpConnector {
  Mono<ClientHttpResponse> connect(HttpMethod paramHttpMethod, URI paramURI, Function<? super ClientHttpRequest, Mono<Void>> paramFunction);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/reactive/ClientHttpConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */