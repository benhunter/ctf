package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public interface DocumentLoader {
  Document loadDocument(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler, int paramInt, boolean paramBoolean) throws Exception;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DocumentLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */