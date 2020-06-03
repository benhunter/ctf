package org.springframework.web.servlet.support;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;

public interface RequestDataValueProcessor {
  String processAction(HttpServletRequest paramHttpServletRequest, String paramString1, String paramString2);
  
  String processFormFieldValue(HttpServletRequest paramHttpServletRequest, @Nullable String paramString1, String paramString2, String paramString3);
  
  @Nullable
  Map<String, String> getExtraHiddenFields(HttpServletRequest paramHttpServletRequest);
  
  String processUrl(HttpServletRequest paramHttpServletRequest, String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/RequestDataValueProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */