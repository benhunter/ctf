package org.springframework.scripting;

import java.io.IOException;
import org.springframework.lang.Nullable;

public interface ScriptSource {
  String getScriptAsString() throws IOException;
  
  boolean isModified();
  
  @Nullable
  String suggestedClassName();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/ScriptSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */