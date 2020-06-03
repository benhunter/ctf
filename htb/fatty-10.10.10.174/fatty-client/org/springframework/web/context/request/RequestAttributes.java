package org.springframework.web.context.request;

import org.springframework.lang.Nullable;

public interface RequestAttributes {
  public static final int SCOPE_REQUEST = 0;
  
  public static final int SCOPE_SESSION = 1;
  
  public static final String REFERENCE_REQUEST = "request";
  
  public static final String REFERENCE_SESSION = "session";
  
  @Nullable
  Object getAttribute(String paramString, int paramInt);
  
  void setAttribute(String paramString, Object paramObject, int paramInt);
  
  void removeAttribute(String paramString, int paramInt);
  
  String[] getAttributeNames(int paramInt);
  
  void registerDestructionCallback(String paramString, Runnable paramRunnable, int paramInt);
  
  @Nullable
  Object resolveReference(String paramString);
  
  String getSessionId();
  
  Object getSessionMutex();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/RequestAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */