package org.springframework.beans;

import java.util.Map;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

public interface PropertyAccessor {
  public static final String NESTED_PROPERTY_SEPARATOR = ".";
  
  public static final char NESTED_PROPERTY_SEPARATOR_CHAR = '.';
  
  public static final String PROPERTY_KEY_PREFIX = "[";
  
  public static final char PROPERTY_KEY_PREFIX_CHAR = '[';
  
  public static final String PROPERTY_KEY_SUFFIX = "]";
  
  public static final char PROPERTY_KEY_SUFFIX_CHAR = ']';
  
  boolean isReadableProperty(String paramString);
  
  boolean isWritableProperty(String paramString);
  
  @Nullable
  Class<?> getPropertyType(String paramString) throws BeansException;
  
  @Nullable
  TypeDescriptor getPropertyTypeDescriptor(String paramString) throws BeansException;
  
  @Nullable
  Object getPropertyValue(String paramString) throws BeansException;
  
  void setPropertyValue(String paramString, @Nullable Object paramObject) throws BeansException;
  
  void setPropertyValue(PropertyValue paramPropertyValue) throws BeansException;
  
  void setPropertyValues(Map<?, ?> paramMap) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues, boolean paramBoolean) throws BeansException;
  
  void setPropertyValues(PropertyValues paramPropertyValues, boolean paramBoolean1, boolean paramBoolean2) throws BeansException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/PropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */