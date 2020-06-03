package org.springframework.jmx.export;

import javax.management.ObjectName;

public interface MBeanExportOperations {
  ObjectName registerManagedResource(Object paramObject) throws MBeanExportException;
  
  void registerManagedResource(Object paramObject, ObjectName paramObjectName) throws MBeanExportException;
  
  void unregisterManagedResource(ObjectName paramObjectName);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/MBeanExportOperations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */