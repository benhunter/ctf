package org.springframework.web.context.request;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.springframework.lang.Nullable;

public interface WebRequest extends RequestAttributes {
  @Nullable
  String getHeader(String paramString);
  
  @Nullable
  String[] getHeaderValues(String paramString);
  
  Iterator<String> getHeaderNames();
  
  @Nullable
  String getParameter(String paramString);
  
  @Nullable
  String[] getParameterValues(String paramString);
  
  Iterator<String> getParameterNames();
  
  Map<String, String[]> getParameterMap();
  
  Locale getLocale();
  
  String getContextPath();
  
  @Nullable
  String getRemoteUser();
  
  @Nullable
  Principal getUserPrincipal();
  
  boolean isUserInRole(String paramString);
  
  boolean isSecure();
  
  boolean checkNotModified(long paramLong);
  
  boolean checkNotModified(String paramString);
  
  boolean checkNotModified(@Nullable String paramString, long paramLong);
  
  String getDescription(boolean paramBoolean);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/WebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */