package org.springframework.cglib.core;

public interface NamingPolicy {
  String getClassName(String paramString1, String paramString2, Object paramObject, Predicate paramPredicate);
  
  boolean equals(Object paramObject);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/core/NamingPolicy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */