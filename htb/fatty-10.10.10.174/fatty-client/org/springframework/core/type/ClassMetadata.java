package org.springframework.core.type;

import org.springframework.lang.Nullable;

public interface ClassMetadata {
  String getClassName();
  
  boolean isInterface();
  
  boolean isAnnotation();
  
  boolean isAbstract();
  
  boolean isConcrete();
  
  boolean isFinal();
  
  boolean isIndependent();
  
  boolean hasEnclosingClass();
  
  @Nullable
  String getEnclosingClassName();
  
  boolean hasSuperClass();
  
  @Nullable
  String getSuperClassName();
  
  String[] getInterfaceNames();
  
  String[] getMemberClassNames();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/ClassMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */