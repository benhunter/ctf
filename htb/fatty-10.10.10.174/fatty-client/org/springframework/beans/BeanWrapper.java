package org.springframework.beans;

import java.beans.PropertyDescriptor;

public interface BeanWrapper extends ConfigurablePropertyAccessor {
  void setAutoGrowCollectionLimit(int paramInt);
  
  int getAutoGrowCollectionLimit();
  
  Object getWrappedInstance();
  
  Class<?> getWrappedClass();
  
  PropertyDescriptor[] getPropertyDescriptors();
  
  PropertyDescriptor getPropertyDescriptor(String paramString) throws InvalidPropertyException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/BeanWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */