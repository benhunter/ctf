package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.util.concurrent.ListenableFuture;

@Deprecated
public interface AsyncClientHttpRequestInterceptor {
  ListenableFuture<ClientHttpResponse> intercept(HttpRequest paramHttpRequest, byte[] paramArrayOfbyte, AsyncClientHttpRequestExecution paramAsyncClientHttpRequestExecution) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/AsyncClientHttpRequestInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */