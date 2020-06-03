package org.springframework.web.cors;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

public interface CorsProcessor {
  boolean processRequest(@Nullable CorsConfiguration paramCorsConfiguration, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse) throws IOException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/cors/CorsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */