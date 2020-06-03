package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpRequest;

@FunctionalInterface
public interface RequestCallback {
  void doWithRequest(ClientHttpRequest paramClientHttpRequest) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/RequestCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */