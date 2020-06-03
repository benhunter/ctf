package org.springframework.web.servlet.view.script;

import java.nio.charset.Charset;
import javax.script.ScriptEngine;
import org.springframework.lang.Nullable;

public interface ScriptTemplateConfig {
  @Nullable
  ScriptEngine getEngine();
  
  @Nullable
  String getEngineName();
  
  @Nullable
  Boolean isSharedEngine();
  
  @Nullable
  String[] getScripts();
  
  @Nullable
  String getRenderObject();
  
  @Nullable
  String getRenderFunction();
  
  @Nullable
  String getContentType();
  
  @Nullable
  Charset getCharset();
  
  @Nullable
  String getResourceLoaderPath();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/script/ScriptTemplateConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */