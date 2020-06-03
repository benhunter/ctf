package org.springframework.core.env;

import java.util.Map;

public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
  void setActiveProfiles(String... paramVarArgs);
  
  void addActiveProfile(String paramString);
  
  void setDefaultProfiles(String... paramVarArgs);
  
  MutablePropertySources getPropertySources();
  
  Map<String, Object> getSystemProperties();
  
  Map<String, Object> getSystemEnvironment();
  
  void merge(ConfigurableEnvironment paramConfigurableEnvironment);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/ConfigurableEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */