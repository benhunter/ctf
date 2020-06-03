package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {
  void registerBeanDefinitions(Document paramDocument, XmlReaderContext paramXmlReaderContext) throws BeanDefinitionStoreException;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/BeanDefinitionDocumentReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */