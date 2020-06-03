package org.springframework.core.env;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.lang.Nullable;

public interface ConfigurablePropertyResolver extends PropertyResolver {
  ConfigurableConversionService getConversionService();
  
  void setConversionService(ConfigurableConversionService paramConfigurableConversionService);
  
  void setPlaceholderPrefix(String paramString);
  
  void setPlaceholderSuffix(String paramString);
  
  void setValueSeparator(@Nullable String paramString);
  
  void setIgnoreUnresolvableNestedPlaceholders(boolean paramBoolean);
  
  void setRequiredProperties(String... paramVarArgs);
  
  void validateRequiredProperties() throws MissingRequiredPropertiesException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/ConfigurablePropertyResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */