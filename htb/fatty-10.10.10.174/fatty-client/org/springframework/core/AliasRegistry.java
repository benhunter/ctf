package org.springframework.core;

public interface AliasRegistry {
  void registerAlias(String paramString1, String paramString2);
  
  void removeAlias(String paramString);
  
  boolean isAlias(String paramString);
  
  String[] getAliases(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/AliasRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */