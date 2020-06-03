package org.springframework.scripting;

import java.io.IOException;
import org.springframework.lang.Nullable;

public interface ScriptFactory {
  String getScriptSourceLocator();
  
  @Nullable
  Class<?>[] getScriptInterfaces();
  
  boolean requiresConfigInterface();
  
  @Nullable
  Object getScriptedObject(ScriptSource paramScriptSource, @Nullable Class<?>... paramVarArgs) throws IOException, ScriptCompilationException;
  
  @Nullable
  Class<?> getScriptedObjectType(ScriptSource paramScriptSource) throws IOException, ScriptCompilationException;
  
  boolean requiresScriptedObjectRefresh(ScriptSource paramScriptSource);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/ScriptFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */