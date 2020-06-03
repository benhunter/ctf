package org.springframework.jmx.export.naming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public interface SelfNaming {
  ObjectName getObjectName() throws MalformedObjectNameException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/naming/SelfNaming.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */