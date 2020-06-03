package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

public interface UnrecognizedElementHandler {
  boolean parseUnrecognizedElement(Element paramElement, Properties paramProperties) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/xml/UnrecognizedElementHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */