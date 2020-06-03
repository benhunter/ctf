/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import java.util.NoSuchElementException;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.springframework.util.ClassUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractXMLEventReader
/*    */   implements XMLEventReader
/*    */ {
/*    */   private boolean closed;
/*    */   
/*    */   public Object next() {
/*    */     try {
/* 40 */       return nextEvent();
/*    */     }
/* 42 */     catch (XMLStreamException ex) {
/* 43 */       throw new NoSuchElementException(ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 49 */     throw new UnsupportedOperationException("remove not supported on " + 
/* 50 */         ClassUtils.getShortName(getClass()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getProperty(String name) throws IllegalArgumentException {
/* 59 */     throw new IllegalArgumentException("Property not supported: [" + name + "]");
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 64 */     this.closed = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void checkIfClosed() throws XMLStreamException {
/* 73 */     if (this.closed)
/* 74 */       throw new XMLStreamException("XMLEventReader has been closed"); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/AbstractXMLEventReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */