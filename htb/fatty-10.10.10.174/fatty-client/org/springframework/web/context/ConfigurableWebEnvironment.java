package org.springframework.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;

public interface ConfigurableWebEnvironment extends ConfigurableEnvironment {
  void initPropertySources(@Nullable ServletContext paramServletContext, @Nullable ServletConfig paramServletConfig);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/ConfigurableWebEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */