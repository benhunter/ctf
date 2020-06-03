package org.springframework.scripting;

import java.util.Map;
import org.springframework.lang.Nullable;

public interface ScriptEvaluator {
  @Nullable
  Object evaluate(ScriptSource paramScriptSource) throws ScriptCompilationException;
  
  @Nullable
  Object evaluate(ScriptSource paramScriptSource, @Nullable Map<String, Object> paramMap) throws ScriptCompilationException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/ScriptEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */