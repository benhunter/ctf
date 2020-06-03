package org.springframework.cglib.transform.impl;

public interface FieldProvider {
  String[] getFieldNames();
  
  Class[] getFieldTypes();
  
  void setField(int paramInt, Object paramObject);
  
  Object getField(int paramInt);
  
  void setField(String paramString, Object paramObject);
  
  Object getField(String paramString);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cglib/transform/impl/FieldProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */