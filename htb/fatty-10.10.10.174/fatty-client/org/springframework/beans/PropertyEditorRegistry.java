package org.springframework.beans;

import java.beans.PropertyEditor;
import org.springframework.lang.Nullable;

public interface PropertyEditorRegistry {
  void registerCustomEditor(Class<?> paramClass, PropertyEditor paramPropertyEditor);
  
  void registerCustomEditor(@Nullable Class<?> paramClass, @Nullable String paramString, PropertyEditor paramPropertyEditor);
  
  @Nullable
  PropertyEditor findCustomEditor(@Nullable Class<?> paramClass, @Nullable String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyEditorRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */