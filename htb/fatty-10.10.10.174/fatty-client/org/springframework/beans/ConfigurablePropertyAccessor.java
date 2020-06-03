package org.springframework.beans;

import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;

public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {
  void setConversionService(@Nullable ConversionService paramConversionService);
  
  @Nullable
  ConversionService getConversionService();
  
  void setExtractOldValueForEditor(boolean paramBoolean);
  
  boolean isExtractOldValueForEditor();
  
  void setAutoGrowNestedPaths(boolean paramBoolean);
  
  boolean isAutoGrowNestedPaths();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/ConfigurablePropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */