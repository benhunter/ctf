package org.springframework.http.server.reactive;

import java.security.cert.X509Certificate;
import org.springframework.lang.Nullable;

public interface SslInfo {
  @Nullable
  String getSessionId();
  
  @Nullable
  X509Certificate[] getPeerCertificates();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/SslInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */