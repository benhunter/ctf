package org.springframework.beans.factory.parsing;

import java.util.EventListener;

public interface ReaderEventListener extends EventListener {
  void defaultsRegistered(DefaultsDefinition paramDefaultsDefinition);
  
  void componentRegistered(ComponentDefinition paramComponentDefinition);
  
  void aliasRegistered(AliasDefinition paramAliasDefinition);
  
  void importProcessed(ImportDefinition paramImportDefinition);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ReaderEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */